package org.tal.basiccircuits;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.tal.redstonechips.Circuit;
import org.tal.redstonechips.parsing.UnitParser;

/**
 *
 * @author Tal Eisenberg
 */
public class clock extends Circuit {
    private long freq;
    private boolean running = false;
    private BitSet onBits, offBits;
    private TickThread thread = new TickThread();

    @Override
    public void inputChange(int inIdx, boolean newLevel) {
        // if not running any change from low to high will start it.

        if (newLevel) { // change from low to high
            if (!running) startClock();
        } else {
            if (running && inputBits.isEmpty()) stopClock();
        }
    }

    @Override
    public boolean init(Player player, String[] args) {
        // one argument for duration. number of inputs should match number of outputs.
        if (args.length==0) freq = 500; // 1 sec default
        else freq = Math.round(UnitParser.parse(args[0])/2);

        if (inputs.length!=outputs.length) {
            error(player, "Expecting the same amount of inputs and outputs.");
            return false;
        }

        onBits = new BitSet(inputs.length);
        offBits = new BitSet(inputs.length);

        onBits.set(0, inputs.length);
        offBits.clear();
        if (player!=null) player.sendMessage(ChatColor.GREEN + "Clock will tick every " + freq*2 + " milliseconds.");

        return true;
    }

    @Override
    public void circuitDestroyed() {
        if (running) {
            stopClock();
        }
    }

    private void startClock() {
        thread.start();
        running = true;
    }

    private void stopClock() {
        thread.interrupt();
        thread = new TickThread();
        running = false;
    }

    @Override
    public void loadState(Map<String, String> state) {
        inputBits = Circuit.loadBitSet(state, "inputBits", inputs.length);

        if (inputBits.isEmpty() && running) stopClock();
        else if (!inputBits.isEmpty() && !running) startClock();
    }

    @Override
    public Map<String, String> saveState() {
        return Circuit.storeBitSet(new HashMap<String, String>(), "inputBits", inputBits, inputs.length);
    }

    class TickThread extends Thread {
        boolean state = false;
        @Override
        public void run() {
            state = true;
            try {
                while(true) {
                    update();
                    Thread.sleep(freq);
                    state = !state;
                }
            } catch (InterruptedException ie) {
                state = false;
                update();
            }
        }

        private void update() {
            if (state) { // turn on any output whose input is on
                BitSet out = (BitSet)inputBits.clone();
                out.and(onBits);
                synchronized(this) {
                    sendBitSet(0, outputs.length, out);
                }
            } else { // just clear everything
                synchronized(this) {
                    sendBitSet(0, outputs.length, offBits);
                }
            }
        }

    }
}
