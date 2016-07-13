package uk.co.dyadica.ev3api;

import com.unity3d.player.UnityPlayer;

public final class UnityMessage
{
    public enum StringID
    {
        bluetooth_required,
        bluetooth_found,
        bluetooth_init,
        bluetooth_try_again
    }

    private static final String UnityGameObject = "EV3Manager";

    public static void throwBluetoothConnectionError(BluetoothConnection.ConnectionError errorMessage)
    {
        sendMessageToUnity("throwBluetoothConnectionError", errorMessage.name());
    }

    public static void throwBluetoothConnected(boolean connectionState)
    {
        sendMessageToUnity("throwBluetoothConnected", String.valueOf(connectionState));
    }

    public static void throwBluetoothAlert(boolean isConnected)
    {
        sendMessageToUnity("throwBluetoothAlert", String.valueOf(isConnected));
    }

    public static void throwDebugUpdate(String message)
    {
        sendMessageToUnity("throwDebugUpdate", message);
    }

    public static void throwButtonUpdate(EV3Button button)
    {
        sendMessageToUnity("throwButtonUpdate", button.name + " " + button.pressed);
    }

    public static void throwPortSensorUpdate(String portSensorUpdate)
    {
        sendMessageToUnity("throwPortSensorUpdate", portSensorUpdate);
    }

    public static void throwPortDeviceUpdate(String portDeviceUpdate)
    {
        sendMessageToUnity("throwPortDeviceUpdate", portDeviceUpdate);
    }

    public static void throwToast(StringID stringID)
    {
        sendMessageToUnity("throwToast", stringID.name());
    }

    private static void sendMessageToUnity(String event, String message)
    {
        UnityPlayer.UnitySendMessage(UnityGameObject, event, message);
    }
}
