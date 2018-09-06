package com.danxionglei.fulldisplaybackgesture;

/**
 * @author damonlei
 */
public class LogDelegate {

    public interface ILog {
        void v(String tag, String msg, Object... args);
        void d(String tag, String msg, Object... args);
        void i(String tag, String msg, Object... args);
        void w(String tag, String msg, Object... args);
        void e(String tag, String msg, Object... args);
        void printStackTrace(String tag, Throwable t, String msg, Object... args);
    }

    private static class DefaultLog implements ILog {

        @Override
        public void v(String tag, String msg, Object... args) {
            try {
                android.util.Log.v(tag, ((args != null && args.length > 0) ? String.format(msg, args) : msg));
            } catch (Exception e) {
                android.util.Log.e(tag, "", e);
            }
        }

        @Override
        public void d(String tag, String msg, Object... args) {
            try {
                android.util.Log.d(tag, ((args != null && args.length > 0) ? String.format(msg, args) : msg));
            } catch (Exception e) {
                android.util.Log.e(tag, "", e);
            }
        }

        @Override
        public void i(String tag, String msg, Object... args) {
            try {
                android.util.Log.i(tag, ((args != null && args.length > 0) ? String.format(msg, args) : msg));
            } catch (Exception e) {
                android.util.Log.e(tag, "", e);
            }
        }

        @Override
        public void w(String tag, String msg, Object... args) {
            try {
                android.util.Log.w(tag, ((args != null && args.length > 0) ? String.format(msg, args) : msg));
            } catch (Exception e) {
                android.util.Log.e(tag, "", e);
            }
        }

        @Override
        public void e(String tag, String msg, Object... args) {
            try {
                android.util.Log.e(tag, ((args != null && args.length > 0) ? String.format(msg, args) : msg));
            } catch (Exception e) {
                android.util.Log.e(tag, "", e);
            }
        }

        @Override
        public void printStackTrace(String tag, Throwable t, String msg, Object... args) {
            if(t != null) {
                try {
                    android.util.Log.e(tag, ((args != null && args.length > 0) ? String.format(msg, args) : msg), t);
                } catch (Exception e) {
                    android.util.Log.e(tag, "", e);
                }
            }
        }
    }

    public static class Log {
        private static ILog sImp = new DefaultLog();

        public static void setImp(ILog imp) {
            if(imp != null) {
                sImp = imp;
            }
        }

        public static void v(String tag, String msg, Object... args) {
            sImp.v(tag, msg, args);
        }

        public static void d(String tag, String msg, Object... args) {
            sImp.d(tag, msg, args);
        }

        public static void i(String tag, String msg, Object... args) {
            sImp.i(tag, msg, args);
        }

        public static void w(String tag, String msg, Object... args) {
            sImp.w(tag, msg, args);
        }

        public static void e(String tag, String msg, Object... args) {
            sImp.e(tag, msg, args);
        }

        public static void printStackTrace(String tag, Throwable t, String msg, Object... args) {
            sImp.printStackTrace(tag, t, msg, args);
        }
    }
}
