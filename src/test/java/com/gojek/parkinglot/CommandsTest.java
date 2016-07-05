package com.gojek.parkinglot;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by prasadus on 26/06/16.
 */
public class CommandsTest {
    Commands commands = new Commands();
    @Test
    public void checkCommandInList() throws Exception {
        assertFalse(commands.commandsMap.isEmpty());
        assertTrue(commands.commandsMap.containsKey("create_parking_lot"));
        assertFalse(commands.commandsMap.containsKey("mytestcommand"));
    }
}