package org.lwjgl.input;

import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Mouse {

    private static class MouseEvent {
        final int button;
        final boolean state;
        final int x, y;
        final int dx, dy;
        final int dwheel;
        final long nanos;

        MouseEvent(int button, boolean state, int x, int y, int dx, int dy, int dwheel) {
            this.button = button;
            this.state = state;
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.dwheel = dwheel;
            this.nanos = System.nanoTime();
        }
    }

    private static final ConcurrentLinkedQueue<MouseEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private static MouseEvent currentEvent = null;

    private static boolean created = false;
    private static boolean grabbed = false;
    private static boolean insideWindow = true;

    private static long windowHandle = 0;
    private static int displayHeight = 480;

    private static double cursorX = 0, cursorY = 0;
    private static double prevX = 0, prevY = 0;
    private static int dxAccum = 0, dyAccum = 0;
    private static int dwheelAccum = 0;
    private static final boolean[] buttonStates = new boolean[8];

    /** Initialize with a GLFW window handle. Sets up mouse callbacks. */
    public static void init(long window, int height) {
        if (created) return;
        windowHandle = window;
        displayHeight = height;

        double[] xpos = new double[1], ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);
        cursorX = xpos[0];
        cursorY = ypos[0];
        prevX = cursorX;
        prevY = cursorY;

        GLFW.glfwSetCursorPosCallback(window, (win, xp, yp) -> {
            double dx = xp - prevX;
            double dy = yp - prevY;
            prevX = xp;
            prevY = yp;
            cursorX = xp;
            cursorY = yp;
            dxAccum += (int) dx;
            dyAccum -= (int) dy; // Invert Y: LWJGL2 has origin at bottom-left

            int convY = displayHeight - 1 - (int) yp;
            eventQueue.add(new MouseEvent(-1, false, (int) xp, convY, (int) dx, -(int) dy, 0));
        });

        GLFW.glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
            if (button >= 0 && button < buttonStates.length) {
                boolean pressed = (action == GLFW.GLFW_PRESS);
                buttonStates[button] = pressed;
                int convY = displayHeight - 1 - (int) cursorY;
                eventQueue.add(new MouseEvent(button, pressed, (int) cursorX, convY, 0, 0, 0));
            }
        });

        GLFW.glfwSetScrollCallback(window, (win, xoffset, yoffset) -> {
            int wheel = (int) (yoffset * 120); // LWJGL2 uses 120 units per notch
            dwheelAccum += wheel;
            int convY = displayHeight - 1 - (int) cursorY;
            eventQueue.add(new MouseEvent(-1, false, (int) cursorX, convY, 0, 0, wheel));
        });

        GLFW.glfwSetCursorEnterCallback(window, (win, entered) -> {
            insideWindow = entered;
        });

        created = true;
    }

    /** Update the display height (call when window is resized). */
    public static void updateDisplayHeight(int height) {
        displayHeight = height;
    }

    public static void destroy() {
        if (!created) return;
        eventQueue.clear();
        currentEvent = null;
        created = false;
        grabbed = false;
    }

    public static boolean isCreated() {
        return created;
    }

    // --- Event queue ---

    public static boolean next() {
        currentEvent = eventQueue.poll();
        return currentEvent != null;
    }

    public static int getEventButton() {
        return currentEvent != null ? currentEvent.button : -1;
    }

    public static boolean getEventButtonState() {
        return currentEvent != null && currentEvent.state;
    }

    public static int getEventX() {
        return currentEvent != null ? currentEvent.x : getX();
    }

    public static int getEventY() {
        return currentEvent != null ? currentEvent.y : getY();
    }

    public static int getEventDX() {
        return currentEvent != null ? currentEvent.dx : 0;
    }

    public static int getEventDY() {
        return currentEvent != null ? currentEvent.dy : 0;
    }

    public static int getEventDWheel() {
        return currentEvent != null ? currentEvent.dwheel : 0;
    }

    public static long getEventNanoseconds() {
        return currentEvent != null ? currentEvent.nanos : System.nanoTime();
    }

    // --- Direct state queries ---

    public static int getX() {
        return (int) cursorX;
    }

    public static int getY() {
        return displayHeight - 1 - (int) cursorY; // Bottom-left origin
    }

    public static int getDX() {
        int dx = dxAccum;
        dxAccum = 0;
        return dx;
    }

    public static int getDY() {
        int dy = dyAccum;
        dyAccum = 0;
        return dy;
    }

    public static int getDWheel() {
        int dw = dwheelAccum;
        dwheelAccum = 0;
        return dw;
    }

    public static boolean isButtonDown(int button) {
        return button >= 0 && button < buttonStates.length && buttonStates[button];
    }

    public static int getButtonCount() {
        return buttonStates.length;
    }

    public static String getButtonName(int button) {
        switch (button) {
            case 0: return "BUTTON0";
            case 1: return "BUTTON1";
            case 2: return "BUTTON2";
            default: return "BUTTON" + button;
        }
    }

    public static int getButtonIndex(String name) {
        if (name == null) return -1;
        if (name.startsWith("BUTTON") && name.length() > 6) {
            try {
                return Integer.parseInt(name.substring(6));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    // --- Grab ---

    public static void setGrabbed(boolean grab) {
        grabbed = grab;
        if (windowHandle != 0) {
            GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR,
                grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
            if (grab) {
                dxAccum = 0;
                dyAccum = 0;
                double[] xpos = new double[1], ypos = new double[1];
                GLFW.glfwGetCursorPos(windowHandle, xpos, ypos);
                prevX = xpos[0];
                prevY = ypos[0];
            }
        }
    }

    public static boolean isGrabbed() {
        return grabbed;
    }

    // --- Cursor position ---

    public static void setCursorPosition(int x, int y) {
        if (windowHandle != 0) {
            int glfwY = displayHeight - 1 - y; // Convert from bottom-left to top-left
            GLFW.glfwSetCursorPos(windowHandle, x, glfwY);
            cursorX = x;
            cursorY = glfwY;
            prevX = x;
            prevY = glfwY;
        }
    }

    public static boolean isInsideWindow() {
        return insideWindow;
    }

    public static void poll() {
        // GLFW events are polled via glfwPollEvents in the main loop
    }
}
