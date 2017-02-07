package com.lrchao.utils;

import android.view.KeyEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Description: ADB命令
 * http://blog.idhyt.com/2015/10/31/android-adb-shell-command/
 * http://adbshell.com/
 *
 * @author lrc19860926@gmail.com
 * @date 2016/11/8 下午3:44
 */

public final class ShellUtils {

    /**
     * 强制停止 并清除
     */
    public static final String COMMAND_PM_CLEAR = "pm clear ";

    /**
     * 杀掉进程
     */
    public static final String COMMAND_KILL = "kill -9 ";

    /**
     * 获取内存状态
     */
    public static final String COMMAND_CAT_MEMINFO = "cat /proc/meminfo";

    /**
     * 获取进程列表
     */
    public static final String COMMAND_PS = "ps";


    /**
     * 模拟点击动作
     */
    public static final String COMMAND_INPUT_TAP = "input tap ";

    /**
     * 获取log
     */
    public static final String COMMAND_GET_LOG = "logcat";

    /**
     * Ping 其中参数-c 1是指ping的次数为1次，-w是指执行的最后期限,单位为秒，也就是执行的时间为1秒，超过1秒则失败.
     */
    public static final String COMMAND_PING = "ping -c 1 -w 1 ";

    /**
     * 清除log
     */
    public static final String COMMAND_LOGCAT_C = "logcat -c";

    /**
     *
     */
    public static final String COMMAND_CHMOD_600 = "chmod 600 ";

    /**
     * 重启
     */
    public static final String COMMAND_REBOOT = "reboot";

    /**
     * 复制-粘贴
     */
    public static final String COMMAND_CP = "cp ";

    /**
     * 将system分区重新挂载为可读写分区
     * 操作
     */
    public static final String COMMAND_REMOUNT_RW = "mount -o remount rw ";

    /**
     * 将system分区重新挂载为只读分区
     * 操作
     */
    public static final String COMMAND_REMOUNT_ONLY_READ = "mount -o remount ro ";

    /**
     * 删除文件夹
     */
    public static final String COMMAND_RM = "rm -rf ";

    /**
     * 安装
     */
    public static final String COMMAND_INSTALL = "pm install -r ";

    /**
     * 卸载
     */
    public static final String COMMAND_UNINSTALL = "pm uninstall ";

    /**
     * 结束进程
     */
    public static final String COMMAND_AM_FORCE_STOP = "am force-stop ";

    /**
     * 清空dmesg log
     */
    public static final String COMMAND_DMESG_C = "dmesg -c ";

    /**
     * 调用物理返回键
     */
    public static final String COMMAND_KEYCODE_BACK = "input keyevent " + KeyEvent.KEYCODE_BACK;

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    private static final String COMMAND_SH = "sh";

    /**
     * check whether has root permission
     *
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * execute shell command, default return result msg
     *
     * @param command command
     * @param isRoot  whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     *
     * @param commands command list
     * @param isRoot   whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     *
     * @param commands command array
     * @param isRoot   whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * execute shell command
     *
     * @param command         command
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands        command list
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands        command array
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return <ul>
     * <li>if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and
     * {@link CommandResult#errorMsg} is null.</li>
     * <li>if {@link CommandResult#result} is -1, there maybe some excepiton.</li>
     * </ul>
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }


        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;


        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }


                // do not use os.writeBytes(command), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();


            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of command
     * <ul>
     * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CommandResult#successMsg} means success message of command result</li>
     * <li>{@link CommandResult#errorMsg} means error message of command result</li>
     * </ul>
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    public static class CommandResult {


        /**
         * result of command
         **/
        public int result;
        /**
         * success message of command result
         **/
        public String successMsg;
        /**
         * error message of command result
         **/
        public String errorMsg;


        public CommandResult(int result) {
            this.result = result;
        }


        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

}
