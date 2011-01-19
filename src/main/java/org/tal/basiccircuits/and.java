package org.tal.basiccircuits;


import java.util.BitSet;
import org.tal.redstonechips.BitSetCircuit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tal Eisenberg
 */
public class and extends BitSetCircuit {

    @Override
    protected void bitSetChanged(int bitSetIdx, BitSet set) {
        BitSet out = (BitSet)inputBitSets[0].clone();
        for (int i=1; i<this.inputBitSets.length; i++) {
            out.and(inputBitSets[i]);
        }
        this.sendBitSet(out);
    }

}