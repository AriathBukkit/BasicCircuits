package org.tal.basiccircuits;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.tal.redstonechips.circuit.Circuit;
import org.tal.redstonechips.util.BitSet7;
import org.tal.redstonechips.util.BitSetUtils;

/**
 *
 * @author Tal Eisenberg
 */
public class flipflop extends Circuit {
    private boolean resetPinMode = false;
    private BitSet7 resetBitSet = new BitSet7();

    @Override
    public void inputChange(int inIdx, boolean newLevel) {
        if (newLevel) {
            if (resetPinMode) {
                if (inIdx == 0) { // reset
                    this.sendBitSet(resetBitSet);
                } else {
                    this.sendOutput(inIdx-1, !outputBits.get(inIdx-1));
                }
            } else this.sendOutput(inIdx, !outputBits.get(inIdx));
        }

    }

    @Override
    public boolean init(CommandSender sender, String[] args) {
        if (outputs.length!=inputs.length && inputs.length!=outputs.length+1) {
            error(sender, "Expecting the same number of inputs and outputs or one extra input reset pin.");
            return false;
        }

        resetPinMode = (inputs.length==outputs.length+1);
        return true;
    }

    @Override
    protected boolean isStateless() {
        return false;
    }

    @Override
    public Map<String, String> getInternalState() {
        Map<String,String> state = new HashMap<String,String>();

        BitSetUtils.bitSetToMap(state, "outputBits", outputBits, outputs.length);

        return state;
    }

    @Override
    public void setInternalState(Map<String, String> state) {
        if (state.containsKey("outputBits"))
            outputBits = BitSetUtils.mapToBitSet(state, "outputBits");
    }
}
