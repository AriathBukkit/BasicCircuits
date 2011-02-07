/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tal.basiccircuits;

import org.bukkit.entity.Player;
import org.tal.redstonechips.circuit.Circuit;

/**
 *
 * @author Tal Eisenberg
 */
public class comparator extends Circuit {
    private boolean hasConstant = false;
    private boolean identityMode;

    private int constant;

    private int wordLength;

    @Override
    public void inputChange(int inIdx, boolean state) {
        if (hasConstant) {
            // compare inputBits as an unsigned int to the constant.
            compare(Circuit.bitSetToUnsignedInt(inputBits, 0, wordLength), constant);
        } else {
            compare(Circuit.bitSetToUnsignedInt(inputBits, 0, wordLength),
                    Circuit.bitSetToUnsignedInt(inputBits, wordLength, wordLength));
        }
    }

    private void compare(int a, int b) {
        if (identityMode) {
            sendOutput(0, a==b);
        } else {
            if (a<b) {
                sendOutput(0, true);
                sendOutput(1, false);
                sendOutput(2, false);
            } else if (a==b) {
                sendOutput(0, false);
                sendOutput(1, true);
                sendOutput(2, false);
            } else if (a>b) {
                sendOutput(0, false);
                sendOutput(1, false);
                sendOutput(2, true);
            }
        }
    }

    @Override
    protected boolean init(Player player, String[] args) {
        if (outputs.length==1) identityMode = true;
        else if (outputs.length==3) identityMode = false; // magnitude mode
        else {
            error(player, "Expecting 1 output for an identity comparator or 3 outputs for a magnitude comparator");
            return false;
        }

        if (args.length>0) {
            // compare to a constant number
            try {
                constant = Integer.decode(args[0]);
                hasConstant = true;
            } catch (NumberFormatException ne) {
                error(player, "Bad constant argument: " + args[0]);
                return false;
            }
            wordLength = inputs.length;
        } else {
            if (inputs.length%2!=0) {
                error(player, "Expecting an even number of inputs when no sign argument is used.");
                return false;
            }
            wordLength = inputs.length/2;
        }

        return true;
    }

}
