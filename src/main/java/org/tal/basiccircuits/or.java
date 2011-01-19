package org.tal.basiccircuits;


import java.util.BitSet;
import org.tal.redstonechips.BitSetCircuit;

/**
 *
 * @author Tal Eisenberg
 */
public class or extends BitSetCircuit {

    @Override
    protected void bitSetChanged(int bitSetIdx, BitSet set) {
        BitSet out = (BitSet)inputBitSets[0].clone();
        for (int i=1; i<this.inputBitSets.length; i++) {
            out.or(inputBitSets[i]);
        }
        this.sendBitSet(out);
    }

}
