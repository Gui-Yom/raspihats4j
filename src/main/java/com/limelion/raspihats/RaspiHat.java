package com.limelion.raspihats;

import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RaspiHat {

    private final I2CDevice device;

    public RaspiHat(I2CDevice i2cdevice) {

        this.device = i2cdevice;
    }

    public RaspiHat(int i2cBus, byte address) throws IOException, I2CFactory.UnsupportedBusNumberException {

        this.device = I2CFactory.getInstance(i2cBus).getDevice(address);
    }

    /**
     * @param cmd
     * @param data
     *
     * @return the response data or null if there is no response
     */
    public byte[] makeRequest(Command cmd, byte[] data) {

        try {

            device.write(new Frame(cmd, data).getBytes());

            if (cmd != Command.RESET) {
                byte[] response = new byte[4 + cmd.getRespPayloadSize()];
                device.read(response, 0, response.length);
                return new Frame(response).getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return the hat firmware version
     */
    public String getFWVersion() {

        byte[] ver = makeRequest(Command.GET_FIRMWARE_VERSION, null);
        return ver[0] + "." + ver[1] + "." + ver[2];
    }

    /**
     * @return the hat board name
     */
    public String getBoardName() {

        byte[] name = makeRequest(Command.GET_BOARD_NAME, null);
        return new String(name, StandardCharsets.US_ASCII);
    }

    public void boardReset() {

        makeRequest(Command.RESET, null);
    }

    /**
     * @param input the input number to check
     *
     * @return the state of the input
     */
    public boolean getInputState(byte input) {

        byte[] response = makeRequest(Command.DI_GET_CHANNEL_STATE, new byte[] { input });
        if (response == null || response[0] != input) {
            throw new RuntimeException("Request failed !");
        }
        return response[1] > 0;
    }
}
