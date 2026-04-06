package org.lwjgl.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Keyboard {

    // LWJGL 2 key constants
    public static final int KEY_NONE = 0;
    public static final int KEY_ESCAPE = 1;
    public static final int KEY_1 = 2;
    public static final int KEY_2 = 3;
    public static final int KEY_3 = 4;
    public static final int KEY_4 = 5;
    public static final int KEY_5 = 6;
    public static final int KEY_6 = 7;
    public static final int KEY_7 = 8;
    public static final int KEY_8 = 9;
    public static final int KEY_9 = 10;
    public static final int KEY_0 = 11;
    public static final int KEY_MINUS = 12;
    public static final int KEY_EQUALS = 13;
    public static final int KEY_BACK = 14;
    public static final int KEY_TAB = 15;
    public static final int KEY_Q = 16;
    public static final int KEY_W = 17;
    public static final int KEY_E = 18;
    public static final int KEY_R = 19;
    public static final int KEY_T = 20;
    public static final int KEY_Y = 21;
    public static final int KEY_U = 22;
    public static final int KEY_I = 23;
    public static final int KEY_O = 24;
    public static final int KEY_P = 25;
    public static final int KEY_LBRACKET = 26;
    public static final int KEY_RBRACKET = 27;
    public static final int KEY_RETURN = 28;
    public static final int KEY_LCONTROL = 29;
    public static final int KEY_A = 30;
    public static final int KEY_S = 31;
    public static final int KEY_D = 32;
    public static final int KEY_F = 33;
    public static final int KEY_G = 34;
    public static final int KEY_H = 35;
    public static final int KEY_J = 36;
    public static final int KEY_K = 37;
    public static final int KEY_L = 38;
    public static final int KEY_SEMICOLON = 39;
    public static final int KEY_APOSTROPHE = 40;
    public static final int KEY_GRAVE = 41;
    public static final int KEY_LSHIFT = 42;
    public static final int KEY_BACKSLASH = 43;
    public static final int KEY_Z = 44;
    public static final int KEY_X = 45;
    public static final int KEY_C = 46;
    public static final int KEY_V = 47;
    public static final int KEY_B = 48;
    public static final int KEY_N = 49;
    public static final int KEY_M = 50;
    public static final int KEY_COMMA = 51;
    public static final int KEY_PERIOD = 52;
    public static final int KEY_SLASH = 53;
    public static final int KEY_RSHIFT = 54;
    public static final int KEY_MULTIPLY = 55;
    public static final int KEY_LMENU = 56;
    public static final int KEY_SPACE = 57;
    public static final int KEY_CAPITAL = 58;
    public static final int KEY_F1 = 59;
    public static final int KEY_F2 = 60;
    public static final int KEY_F3 = 61;
    public static final int KEY_F4 = 62;
    public static final int KEY_F5 = 63;
    public static final int KEY_F6 = 64;
    public static final int KEY_F7 = 65;
    public static final int KEY_F8 = 66;
    public static final int KEY_F9 = 67;
    public static final int KEY_F10 = 68;
    public static final int KEY_NUMLOCK = 69;
    public static final int KEY_SCROLL = 70;
    public static final int KEY_NUMPAD7 = 71;
    public static final int KEY_NUMPAD8 = 72;
    public static final int KEY_NUMPAD9 = 73;
    public static final int KEY_SUBTRACT = 74;
    public static final int KEY_NUMPAD4 = 75;
    public static final int KEY_NUMPAD5 = 76;
    public static final int KEY_NUMPAD6 = 77;
    public static final int KEY_ADD = 78;
    public static final int KEY_NUMPAD1 = 79;
    public static final int KEY_NUMPAD2 = 80;
    public static final int KEY_NUMPAD3 = 81;
    public static final int KEY_NUMPAD0 = 82;
    public static final int KEY_DECIMAL = 83;
    public static final int KEY_F11 = 87;
    public static final int KEY_F12 = 88;
    public static final int KEY_F13 = 100;
    public static final int KEY_F14 = 101;
    public static final int KEY_F15 = 102;
    public static final int KEY_NUMPADENTER = 156;
    public static final int KEY_RCONTROL = 157;
    public static final int KEY_DIVIDE = 181;
    public static final int KEY_SYSRQ = 183;
    public static final int KEY_RMENU = 184;
    public static final int KEY_PAUSE = 197;
    public static final int KEY_HOME = 199;
    public static final int KEY_UP = 200;
    public static final int KEY_PRIOR = 201;
    public static final int KEY_LEFT = 203;
    public static final int KEY_RIGHT = 205;
    public static final int KEY_END = 207;
    public static final int KEY_DOWN = 208;
    public static final int KEY_NEXT = 209;
    public static final int KEY_INSERT = 210;
    public static final int KEY_DELETE = 211;
    public static final int KEY_LMETA = 219;
    public static final int KEY_RMETA = 220;

    private static class KeyEvent {
        final int key;
        final char character;
        final boolean state;
        final boolean repeat;
        final long nanos;

        KeyEvent(int key, char character, boolean state, boolean repeat) {
            this.key = key;
            this.character = character;
            this.state = state;
            this.repeat = repeat;
            this.nanos = System.nanoTime();
        }
    }

    private static final ConcurrentLinkedQueue<KeyEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private static KeyEvent currentEvent = null;
    private static boolean created = false;
    private static boolean repeatEvents = false;
    private static long windowHandle = 0;

    // GLFW to LWJGL2 and LWJGL2 to GLFW lookup tables
    private static final int[] GLFW_TO_LWJGL2 = new int[GLFW.GLFW_KEY_LAST + 1];
    private static final int[] LWJGL2_TO_GLFW = new int[256];
    private static final Map<Integer, String> KEY_NAMES = new HashMap<>();
    private static final Map<String, Integer> NAME_TO_KEY = new HashMap<>();

    static {
        mapKey(GLFW.GLFW_KEY_ESCAPE, KEY_ESCAPE, "ESCAPE");
        mapKey(GLFW.GLFW_KEY_1, KEY_1, "1"); mapKey(GLFW.GLFW_KEY_2, KEY_2, "2");
        mapKey(GLFW.GLFW_KEY_3, KEY_3, "3"); mapKey(GLFW.GLFW_KEY_4, KEY_4, "4");
        mapKey(GLFW.GLFW_KEY_5, KEY_5, "5"); mapKey(GLFW.GLFW_KEY_6, KEY_6, "6");
        mapKey(GLFW.GLFW_KEY_7, KEY_7, "7"); mapKey(GLFW.GLFW_KEY_8, KEY_8, "8");
        mapKey(GLFW.GLFW_KEY_9, KEY_9, "9"); mapKey(GLFW.GLFW_KEY_0, KEY_0, "0");
        mapKey(GLFW.GLFW_KEY_MINUS, KEY_MINUS, "MINUS");
        mapKey(GLFW.GLFW_KEY_EQUAL, KEY_EQUALS, "EQUALS");
        mapKey(GLFW.GLFW_KEY_BACKSPACE, KEY_BACK, "BACK");
        mapKey(GLFW.GLFW_KEY_TAB, KEY_TAB, "TAB");
        mapKey(GLFW.GLFW_KEY_Q, KEY_Q, "Q"); mapKey(GLFW.GLFW_KEY_W, KEY_W, "W");
        mapKey(GLFW.GLFW_KEY_E, KEY_E, "E"); mapKey(GLFW.GLFW_KEY_R, KEY_R, "R");
        mapKey(GLFW.GLFW_KEY_T, KEY_T, "T"); mapKey(GLFW.GLFW_KEY_Y, KEY_Y, "Y");
        mapKey(GLFW.GLFW_KEY_U, KEY_U, "U"); mapKey(GLFW.GLFW_KEY_I, KEY_I, "I");
        mapKey(GLFW.GLFW_KEY_O, KEY_O, "O"); mapKey(GLFW.GLFW_KEY_P, KEY_P, "P");
        mapKey(GLFW.GLFW_KEY_LEFT_BRACKET, KEY_LBRACKET, "LBRACKET");
        mapKey(GLFW.GLFW_KEY_RIGHT_BRACKET, KEY_RBRACKET, "RBRACKET");
        mapKey(GLFW.GLFW_KEY_ENTER, KEY_RETURN, "RETURN");
        mapKey(GLFW.GLFW_KEY_LEFT_CONTROL, KEY_LCONTROL, "LCONTROL");
        mapKey(GLFW.GLFW_KEY_A, KEY_A, "A"); mapKey(GLFW.GLFW_KEY_S, KEY_S, "S");
        mapKey(GLFW.GLFW_KEY_D, KEY_D, "D"); mapKey(GLFW.GLFW_KEY_F, KEY_F, "F");
        mapKey(GLFW.GLFW_KEY_G, KEY_G, "G"); mapKey(GLFW.GLFW_KEY_H, KEY_H, "H");
        mapKey(GLFW.GLFW_KEY_J, KEY_J, "J"); mapKey(GLFW.GLFW_KEY_K, KEY_K, "K");
        mapKey(GLFW.GLFW_KEY_L, KEY_L, "L");
        mapKey(GLFW.GLFW_KEY_SEMICOLON, KEY_SEMICOLON, "SEMICOLON");
        mapKey(GLFW.GLFW_KEY_APOSTROPHE, KEY_APOSTROPHE, "APOSTROPHE");
        mapKey(GLFW.GLFW_KEY_GRAVE_ACCENT, KEY_GRAVE, "GRAVE");
        mapKey(GLFW.GLFW_KEY_LEFT_SHIFT, KEY_LSHIFT, "LSHIFT");
        mapKey(GLFW.GLFW_KEY_BACKSLASH, KEY_BACKSLASH, "BACKSLASH");
        mapKey(GLFW.GLFW_KEY_Z, KEY_Z, "Z"); mapKey(GLFW.GLFW_KEY_X, KEY_X, "X");
        mapKey(GLFW.GLFW_KEY_C, KEY_C, "C"); mapKey(GLFW.GLFW_KEY_V, KEY_V, "V");
        mapKey(GLFW.GLFW_KEY_B, KEY_B, "B"); mapKey(GLFW.GLFW_KEY_N, KEY_N, "N");
        mapKey(GLFW.GLFW_KEY_M, KEY_M, "M");
        mapKey(GLFW.GLFW_KEY_COMMA, KEY_COMMA, "COMMA");
        mapKey(GLFW.GLFW_KEY_PERIOD, KEY_PERIOD, "PERIOD");
        mapKey(GLFW.GLFW_KEY_SLASH, KEY_SLASH, "SLASH");
        mapKey(GLFW.GLFW_KEY_RIGHT_SHIFT, KEY_RSHIFT, "RSHIFT");
        mapKey(GLFW.GLFW_KEY_KP_MULTIPLY, KEY_MULTIPLY, "MULTIPLY");
        mapKey(GLFW.GLFW_KEY_LEFT_ALT, KEY_LMENU, "LMENU");
        mapKey(GLFW.GLFW_KEY_SPACE, KEY_SPACE, "SPACE");
        mapKey(GLFW.GLFW_KEY_CAPS_LOCK, KEY_CAPITAL, "CAPITAL");
        mapKey(GLFW.GLFW_KEY_F1, KEY_F1, "F1"); mapKey(GLFW.GLFW_KEY_F2, KEY_F2, "F2");
        mapKey(GLFW.GLFW_KEY_F3, KEY_F3, "F3"); mapKey(GLFW.GLFW_KEY_F4, KEY_F4, "F4");
        mapKey(GLFW.GLFW_KEY_F5, KEY_F5, "F5"); mapKey(GLFW.GLFW_KEY_F6, KEY_F6, "F6");
        mapKey(GLFW.GLFW_KEY_F7, KEY_F7, "F7"); mapKey(GLFW.GLFW_KEY_F8, KEY_F8, "F8");
        mapKey(GLFW.GLFW_KEY_F9, KEY_F9, "F9"); mapKey(GLFW.GLFW_KEY_F10, KEY_F10, "F10");
        mapKey(GLFW.GLFW_KEY_NUM_LOCK, KEY_NUMLOCK, "NUMLOCK");
        mapKey(GLFW.GLFW_KEY_SCROLL_LOCK, KEY_SCROLL, "SCROLL");
        mapKey(GLFW.GLFW_KEY_KP_7, KEY_NUMPAD7, "NUMPAD7");
        mapKey(GLFW.GLFW_KEY_KP_8, KEY_NUMPAD8, "NUMPAD8");
        mapKey(GLFW.GLFW_KEY_KP_9, KEY_NUMPAD9, "NUMPAD9");
        mapKey(GLFW.GLFW_KEY_KP_SUBTRACT, KEY_SUBTRACT, "SUBTRACT");
        mapKey(GLFW.GLFW_KEY_KP_4, KEY_NUMPAD4, "NUMPAD4");
        mapKey(GLFW.GLFW_KEY_KP_5, KEY_NUMPAD5, "NUMPAD5");
        mapKey(GLFW.GLFW_KEY_KP_6, KEY_NUMPAD6, "NUMPAD6");
        mapKey(GLFW.GLFW_KEY_KP_ADD, KEY_ADD, "ADD");
        mapKey(GLFW.GLFW_KEY_KP_1, KEY_NUMPAD1, "NUMPAD1");
        mapKey(GLFW.GLFW_KEY_KP_2, KEY_NUMPAD2, "NUMPAD2");
        mapKey(GLFW.GLFW_KEY_KP_3, KEY_NUMPAD3, "NUMPAD3");
        mapKey(GLFW.GLFW_KEY_KP_0, KEY_NUMPAD0, "NUMPAD0");
        mapKey(GLFW.GLFW_KEY_KP_DECIMAL, KEY_DECIMAL, "DECIMAL");
        mapKey(GLFW.GLFW_KEY_F11, KEY_F11, "F11");
        mapKey(GLFW.GLFW_KEY_F12, KEY_F12, "F12");
        mapKey(GLFW.GLFW_KEY_F13, KEY_F13, "F13");
        mapKey(GLFW.GLFW_KEY_F14, KEY_F14, "F14");
        mapKey(GLFW.GLFW_KEY_F15, KEY_F15, "F15");
        mapKey(GLFW.GLFW_KEY_KP_ENTER, KEY_NUMPADENTER, "NUMPADENTER");
        mapKey(GLFW.GLFW_KEY_RIGHT_CONTROL, KEY_RCONTROL, "RCONTROL");
        mapKey(GLFW.GLFW_KEY_KP_DIVIDE, KEY_DIVIDE, "DIVIDE");
        mapKey(GLFW.GLFW_KEY_PRINT_SCREEN, KEY_SYSRQ, "SYSRQ");
        mapKey(GLFW.GLFW_KEY_RIGHT_ALT, KEY_RMENU, "RMENU");
        mapKey(GLFW.GLFW_KEY_PAUSE, KEY_PAUSE, "PAUSE");
        mapKey(GLFW.GLFW_KEY_HOME, KEY_HOME, "HOME");
        mapKey(GLFW.GLFW_KEY_UP, KEY_UP, "UP");
        mapKey(GLFW.GLFW_KEY_PAGE_UP, KEY_PRIOR, "PRIOR");
        mapKey(GLFW.GLFW_KEY_LEFT, KEY_LEFT, "LEFT");
        mapKey(GLFW.GLFW_KEY_RIGHT, KEY_RIGHT, "RIGHT");
        mapKey(GLFW.GLFW_KEY_END, KEY_END, "END");
        mapKey(GLFW.GLFW_KEY_DOWN, KEY_DOWN, "DOWN");
        mapKey(GLFW.GLFW_KEY_PAGE_DOWN, KEY_NEXT, "NEXT");
        mapKey(GLFW.GLFW_KEY_INSERT, KEY_INSERT, "INSERT");
        mapKey(GLFW.GLFW_KEY_DELETE, KEY_DELETE, "DELETE");
        mapKey(GLFW.GLFW_KEY_LEFT_SUPER, KEY_LMETA, "LMETA");
        mapKey(GLFW.GLFW_KEY_RIGHT_SUPER, KEY_RMETA, "RMETA");
    }

    private static void mapKey(int glfwKey, int lwjgl2Key, String name) {
        GLFW_TO_LWJGL2[glfwKey] = lwjgl2Key;
        if (lwjgl2Key < LWJGL2_TO_GLFW.length) {
            LWJGL2_TO_GLFW[lwjgl2Key] = glfwKey;
        }
        KEY_NAMES.put(lwjgl2Key, name);
        NAME_TO_KEY.put(name, lwjgl2Key);
    }

    /** Initialize with a GLFW window handle. Sets up key and char callbacks. */
    public static void init(long window) {
        if (created) return;
        windowHandle = window;

        GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_UNKNOWN) return;
            if (action == GLFW.GLFW_REPEAT && !repeatEvents) return;

            int lwjgl2Key = (key >= 0 && key < GLFW_TO_LWJGL2.length) ? GLFW_TO_LWJGL2[key] : 0;
            boolean pressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
            boolean repeat = (action == GLFW.GLFW_REPEAT);
            eventQueue.add(new KeyEvent(lwjgl2Key, '\0', pressed, repeat));
        });

        GLFW.glfwSetCharCallback(window, (win, codepoint) -> {
            eventQueue.add(new KeyEvent(0, (char) codepoint, true, false));
        });

        created = true;
    }

    public static void destroy() {
        if (!created) return;
        eventQueue.clear();
        currentEvent = null;
        created = false;
    }

    public static boolean isCreated() {
        return created;
    }

    // --- Event queue ---

    public static boolean next() {
        currentEvent = eventQueue.poll();
        return currentEvent != null;
    }

    public static int getEventKey() {
        return currentEvent != null ? currentEvent.key : KEY_NONE;
    }

    public static char getEventCharacter() {
        return currentEvent != null ? currentEvent.character : '\0';
    }

    public static boolean getEventKeyState() {
        return currentEvent != null && currentEvent.state;
    }

    public static long getEventNanoseconds() {
        return currentEvent != null ? currentEvent.nanos : System.nanoTime();
    }

    public static boolean isRepeatEvent() {
        return currentEvent != null && currentEvent.repeat;
    }

    // --- Direct state query ---

    public static boolean isKeyDown(int lwjgl2Key) {
        if (!created || windowHandle == 0) return false;
        int glfwKey = (lwjgl2Key >= 0 && lwjgl2Key < LWJGL2_TO_GLFW.length) ? LWJGL2_TO_GLFW[lwjgl2Key] : 0;
        if (glfwKey == 0) return false;
        return GLFW.glfwGetKey(windowHandle, glfwKey) == GLFW.GLFW_PRESS;
    }

    // --- Repeat events ---

    public static void enableRepeatEvents(boolean enable) {
        repeatEvents = enable;
    }

    public static boolean areRepeatEventsEnabled() {
        return repeatEvents;
    }

    // --- Key name / index ---

    public static String getKeyName(int key) {
        String name = KEY_NAMES.get(key);
        return name != null ? name : "UNKNOWN";
    }

    public static int getKeyIndex(String name) {
        Integer key = NAME_TO_KEY.get(name.toUpperCase());
        return key != null ? key : KEY_NONE;
    }

    public static int getNumKeyboardEvents() {
        return eventQueue.size();
    }

    public static void poll() {
        // GLFW events are polled via glfwPollEvents in the main loop
    }
}
