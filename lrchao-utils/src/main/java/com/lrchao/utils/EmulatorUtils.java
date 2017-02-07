package com.lrchao.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 模拟器处理
 *
 * @author lrc19860926@gmail.com
 * @date 2016/11/13 下午1:02
 */

public final class EmulatorUtils {

    public static final String FILE_NAME_BUILD_PROP = "/system/build.prop";


    private static String[] known_pipes = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"
    };

    private static String[] known_qemu_drivers = {
            "goldfish"
    };

    /**
     * libc_malloc_debug_qemu.so （内存泄露检测有关）
     */
    private static String[] known_files = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
    };

    private static String[] known_numbers = {"15555215554", "15555215556",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580",
            "15555215582", "15555215584",};

    private static String[] known_device_ids = {
            "000000000000000" // 默认ID
    };

    private static String[] known_imsi_ids = {
            "310260000000000" // 默认的 imsi id
    };

    /**
     * 检测“/dev/socket/qemud”，“/dev/qemu_pipe”这两个通道*
     */
    public static boolean checkPipes() {
        for (int i = 0; i < known_pipes.length; i++) {
            String pipes = known_pipes[i];
            File qemu_socket = new File(pipes);
            if (qemu_socket.exists()) {
                LogUtils.e("Result:", "Find pipes!");
                return true;
            }
        }
        return false;
    }

    /**
     * 检测驱动文件内容
     * 读取文件内容，然后检查已知QEmu的驱动程序的列表
     */
    public static boolean checkQEmuDriverFile() {
        File driver_file = new File("/proc/tty/drivers");
        if (driver_file.exists() && driver_file.canRead()) {
            byte[] data = new byte[1024];  //(int)driver_file.length()
            try {
                InputStream inStream = new FileInputStream(driver_file);
                inStream.read(data);
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String driver_data = new String(data);
            for (String known_qemu_driver : EmulatorUtils.known_qemu_drivers) {
                if (driver_data.indexOf(known_qemu_driver) != -1) {
                    LogUtils.e("Result:", "Find know_qemu_drivers!");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测模拟器上特有的几个文件
     */
    public static boolean checkEmulatorFiles() {
        for (int i = 0; i < known_files.length; i++) {
            String file_name = known_files[i];
            File qemu_file = new File(file_name);
            if (qemu_file.exists()) {
                LogUtils.e("Result:", "Find Emulator Files! :" + qemu_file.getAbsolutePath());
                return true;
            }
        }

        return false;
    }

    /**
     * 检测模拟器默认的电话号码
     */
    public static boolean checkPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNumber = telephonyManager.getLine1Number();

        for (String number : known_numbers) {
            if (number.equalsIgnoreCase(phoneNumber)) {
                LogUtils.e("Result:", "Find PhoneNumber!==" + phoneNumber);
                return true;
            }
        }
        return false;
    }

    /**
     * 检测设备IDS 是不是 “000000000000000”
     */
    public static boolean checkDeviceIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String device_ids = telephonyManager.getDeviceId();

        for (String know_deviceId : known_device_ids) {
            if (know_deviceId.equalsIgnoreCase(device_ids)) {
                LogUtils.e("Result:", "Find ids: 000000000000000!");
                return true;
            }
        }
        return false;
    }

    /**
     * 检测imsi id是不是“310260000000000”
     */
    public static boolean checkImsiIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        String imsi_ids = telephonyManager.getSubscriberId();

        for (String know_imsi : known_imsi_ids) {
            if (know_imsi.equalsIgnoreCase(imsi_ids)) {
                LogUtils.e("Result:", "Find imsi ids: 310260000000000!");
                return true;
            }
        }
        return false;
    }

    /**
     * 检测手机上的一些硬件信息
     */
    public static boolean checkEmulatorBuild(Context context) {
        String BOARD = android.os.Build.BOARD;
        String BOOTLOADER = android.os.Build.BOOTLOADER;
        String BRAND = android.os.Build.BRAND;
        String DEVICE = android.os.Build.DEVICE;
        String HARDWARE = android.os.Build.HARDWARE;
        String MODEL = android.os.Build.MODEL;
        String PRODUCT = android.os.Build.PRODUCT;

        if (/*"unknown".equals(BOARD) ||*/
                /*"unknown".equals(BOOTLOADER) ||*/
                "generic".equals(BRAND) ||
                        "generic".equals(DEVICE) ||
                        "sdk".equals(MODEL) ||
                        "sdk".equals(PRODUCT) ||
                        "goldfish".equals(HARDWARE)) {

            LogUtils.e("Result:", "Find Emulator by EmulatorBuild!==" +
                    " BOARD=" + BOARD +
                    " BOOTLOADER=" + BOOTLOADER +
                    " BRAND=" + BRAND +
                    " DEVICE=" + DEVICE +
                    " MODEL=" + MODEL +
                    " PRODUCT=" + PRODUCT +
                    " HARDWARE=" + HARDWARE +
                    " ");
            return true;
        }
        return false;
    }

    //

    /**
     * 检测手机运营商家
     */
    @SuppressWarnings("WrongConstant")
    public static boolean checkOperatorNameAndroid(Context context) {
        String szOperatorName = ((TelephonyManager)
                context.getSystemService("phone")).getNetworkOperatorName();

        if (szOperatorName.toLowerCase().equals("android") == true) {
            LogUtils.e("Result:", "Find Emulator by OperatorName!==" + szOperatorName.toLowerCase());
            return true;
        }
        return false;
    }

    public static boolean checkVirtualBox() {
        String commands[] = {"dmesg | grep -i virtualbox"};
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(commands, false, true);
        String successMsg = commandResult.successMsg;
        if (successMsg != null && !"".equals(successMsg)) {
            if (successMsg.contains("VirtualBox")) {
                return true;
            }
        }

        //夜神模拟器的判断
        String commands2[] = {"cat /system/build.prop"};
        commandResult = ShellUtils.execCommand(commands2, false, true);
        successMsg = commandResult.successMsg;
        if (successMsg != null && successMsg != "") {
            String vboxRegex = "\\bvbox\\b|\\bvbox86\\b|\\bvbox86p\\b|\\bttVM";
            Pattern pattern = Pattern.compile(vboxRegex);
            Matcher matcher = pattern.matcher(successMsg);
            if (matcher.find()) {
                return true;
            }

            //新版夜神模拟器,跟新版4399手游通，新判断
            if (successMsg.contains("KOT49H") && successMsg.contains("x86")) {
                //真机下都有mmcblk0分区，但是模拟器没有分区信息。
                String commands3[] = {"cat /proc/diskstats"};
                commandResult = ShellUtils.execCommand(commands3, false, true);
                successMsg = commandResult.successMsg;
                if (successMsg != null && successMsg != "") {
                    if (!successMsg.contains("mmcblk0")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
