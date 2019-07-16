package com.limelion.raspihats;

public enum Command {

    // Commons
    GET_BOARD_NAME(0x10, 0, 25),
    GET_FIRMWARE_VERSION(0x11, 0, 3),
    GET_STATUS_WORD(0x12, 0, 4),
    RESET(0x13, 0, 0),
    // Watchdog
    CWDT_SET_PERIOD(0x14, 4, 4),
    CWDT_GET_PERIOD(0x15, 0, 4),
    // IRQ
    IRQ_GET_REG(0x16, 0, 5),
    IRQ_SET_REG(0x17, 5, 5),
    // Digital inputs
    DI_GET_ALL_CHANNEL_STATES(0x20, 0, 4),
    DI_GET_CHANNEL_STATE(0x21, 1, 2),
    DI_GET_COUNTER(0x22, 2, 6),
    DI_RESET_COUNTER(0x23, 2, 2),
    DI_RESET_ALL_COUNTERS(0x24, 0, 0),
    // Digital outputs
    DQ_SET_POWER_ON_VALUE(0x30, 4, 4),
    DQ_GET_POWER_ON_VALUE(0x31, 0, 4),
    DQ_SET_SAFETY_VALUE(0x32, 4, 4),
    DQ_GET_SAFETY_VALUE(0x33, 0, 4),
    DQ_SET_ALL_CHANNEL_STATES(0x34, 4, 4),
    DQ_GET_ALL_CHANNEL_STATES(0x35, 0, 4),
    DQ_SET_CHANNEL_STATE(0x36, 2, 2),
    DQ_GET_CHANNEL_STATE(0x37, 1, 2);

    private byte id;
    private int reqPayloadSize;
    private int respPayloadSize;

    Command(int id, int reqPayloadSize, int respPayloadSize) {

        this.id = (byte) id;
        this.reqPayloadSize = reqPayloadSize;
        this.respPayloadSize = respPayloadSize;
    }

    public static Command valueOf(byte id) {

        for (Command c : values())
            if (c.id == id)
                return c;
        throw new IllegalArgumentException("Invalid command ID.");
    }

    public byte getId() {

        return id;
    }

    public int getReqPayloadSize() {

        return reqPayloadSize;
    }

    public int getRespPayloadSize() {

        return respPayloadSize;
    }

    public boolean hasDataField() {

        return respPayloadSize > 0;
    }
}
