package com.limelion.raspihats;

import java.util.Arrays;

public class Frame {

    private static byte id = -1;

    private final byte frameId;
    private final Command command;
    private final byte[] data;

    private Frame(byte frameId, Command command, byte[] data) {

        this.frameId = frameId;
        this.command = command;
        this.data = data == null ? new byte[0] : data;
    }

    public Frame(Command command, byte[] data) {

        this(generateId(), command, data);
    }

    /**
     * Parse a frame.
     *
     * @param frame
     */
    public Frame(byte[] frame) {

        if (frame == null || frame.length < 4)
            throw new IllegalArgumentException("Unable to parse frame.");

        this.frameId = frame[0];
        this.command = Command.valueOf(frame[1]);

        // Compare given and computed crc16
        if (!Arrays.equals(Arrays.copyOfRange(frame, frame.length - 2, frame.length),
                           Utils.computeCrc(frame, 0, frame.length - 2))) {
            throw new IllegalArgumentException("Malformed frame. Crc check did not pass.");
        }

        this.data = Arrays.copyOfRange(frame, 2, 2 + command.getRespPayloadSize());
    }

    private static byte generateId() {

        return id = (byte) (id + 1 & 0x7F);
    }

    public byte[] getBytes() {

        byte[] frame = Arrays.copyOf(new byte[] { frameId }, 4 + (data != null ? data.length : 0));
        frame[1] = command.getId();
        if (data != null)
            System.arraycopy(data, 0, frame, 2, data.length);
        System.arraycopy(Utils.computeCrc(frame, 0, 2 + data.length), 0, frame, frame.length - 2, 2);
        return frame;
    }

    public byte getFrameId() {

        return frameId;
    }

    public Command getCommand() {

        return command;
    }

    public byte[] getData() {

        return data;
    }
}
