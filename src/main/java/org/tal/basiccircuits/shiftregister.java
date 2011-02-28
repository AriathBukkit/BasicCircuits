package org.tal.basiccircuits;


import org.bukkit.command.CommandSender;
import org.tal.redstonechips.circuit.Circuit;
import org.tal.redstonechips.util.BitSet7;
import org.tal.redstonechips.util.BitSetUtils;

/**
 *
 * @author Tal Eisenberg
 */
public class shiftregister extends Circuit {
    private BitSet7 register;

    @Override
    public void inputChange(int inIdx, boolean high) {
        if (inIdx==0 && high) { // clock
            BitSetUtils.shiftLeft(register, outputs.length);
            register.set(0, inputBits.get(1));
            sendBitSet(register);
        }
    }

    @Override
    protected boolean init(CommandSender sender, String[] args) {
        if (inputs.length!=2) {
            error(sender, "Expecting two inputs. ");
            return false;
        }


        register = new BitSet7(outputs.length);
        return true;

    }


}
