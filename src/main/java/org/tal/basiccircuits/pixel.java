/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tal.basiccircuits;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.tal.redstonechips.Circuit;

/**
 * // dyes wool if present on output block
 * @author Tal Eisenberg
 */
public class pixel extends Circuit {
    private boolean newInput = true;
    private boolean indexedColor = false;
    private byte[] colorIndex;

    @Override
    public void inputChange(int inIdx, boolean on) {
        if (inIdx==0 && on) { // clock pin
            if (!newInput) return;
            if (outputBlock.getType()==Material.WOOL) {
                updatePixel(outputBlock);
                newInput = false;
            }
        } else newInput = true;
    }

    @Override
    protected boolean init(Player player, String[] args) {
        // needs to have 5 inputs 1 clock 4 data

        if (args.length>0) {
            indexedColor = true;
            colorIndex = new byte[args.length];
            for (int i=0; i<colorIndex.length; i++) {
                try {
                    colorIndex[i] = DyeColor.valueOf(args[i].toUpperCase()).getData();
                } catch (IllegalArgumentException ie) {
                    try {
                        int val = Integer.decode(args[i]);
                        colorIndex[i] = (byte)val;
                    } catch (NumberFormatException ne) {
                        error(player, "Unknown color name: " + args[i]);
                        return false;
                    }
                }

            }
        }

        if (indexedColor) {
            int exp = (int)Math.ceil(Math.log(colorIndex.length)/Math.log(2)) + 1;
            if (inputs.length!=exp) {
                error(player, "Expecting " + exp + " inputs for " + colorIndex.length + " colors. 1 clock pin and " + (exp-1) + " data pins.");
                return false;
            }
        } else if(inputs.length != 5) {
            error(player, "Expecting 5 inputs. 1 clock pin and 4 data pins.");
            return false;
        }

        return true;
    }

    private void updatePixel(Block block) {
        byte color;
        
        if (indexedColor) {
            int index = Circuit.bitSetToUnsignedInt(inputBits, 1, inputs.length-1);
            if (index>=colorIndex.length) return;
            color = colorIndex[index];
        } else 
            color = (byte)Circuit.bitSetToUnsignedInt(inputBits, 1, inputs.length-1);

        block.setData(color);

        Block down = block.getFace(BlockFace.DOWN);
        Block east = block.getFace(BlockFace.EAST);
        Block north = block.getFace(BlockFace.NORTH);
        Block northEast = block.getFace(BlockFace.NORTH_EAST);
        Block northWest = block.getFace(BlockFace.NORTH_WEST);
        Block south = block.getFace(BlockFace.SOUTH);
        Block southEast = block.getFace(BlockFace.SOUTH_EAST);
        Block southWest = block.getFace(BlockFace.SOUTH_WEST);
        Block up = block.getFace(BlockFace.UP);
        Block west = block.getFace(BlockFace.WEST);

        Block upeast = up.getFace(BlockFace.EAST);
        Block upnorth = up.getFace(BlockFace.NORTH);
        Block upnorthEast = up.getFace(BlockFace.NORTH_EAST);
        Block upnorthWest = up.getFace(BlockFace.NORTH_WEST);
        Block upsouth = up.getFace(BlockFace.SOUTH);
        Block upsouthEast = up.getFace(BlockFace.SOUTH_EAST);
        Block upsouthWest = up.getFace(BlockFace.SOUTH_WEST);
        Block upwest = up.getFace(BlockFace.WEST);

        Block downeast = down.getFace(BlockFace.EAST);
        Block downnorth = down.getFace(BlockFace.NORTH);
        Block downnorthEast = down.getFace(BlockFace.NORTH_EAST);
        Block downnorthWest = down.getFace(BlockFace.NORTH_WEST);
        Block downsouth = down.getFace(BlockFace.SOUTH);
        Block downsouthEast = down.getFace(BlockFace.SOUTH_EAST);
        Block downsouthWest = down.getFace(BlockFace.SOUTH_WEST);
        Block downwest = down.getFace(BlockFace.WEST);

        if (down.getType()==Material.WOOL)
            down.setData(color);
        if (east.getType()==Material.WOOL)
            east.setData(color);
        if (north.getType()==Material.WOOL)
            north.setData(color);
        if (northEast.getType()==Material.WOOL)
            northEast.setData(color);
        if (northWest.getType()==Material.WOOL)
            northWest.setData(color);
        if (south.getType()==Material.WOOL)
            south.setData(color);
        if (southEast.getType()==Material.WOOL)
            southEast.setData(color);
        if (west.getType()==Material.WOOL)
            west.setData(color);
        if (up.getType()==Material.WOOL)
            up.setData(color);
        if (southWest.getType()==Material.WOOL)
            southWest.setData(color);

        if (upeast.getType()==Material.WOOL)
            upeast.setData(color);
        if (upnorth.getType()==Material.WOOL)
            upnorth.setData(color);
        if (upnorthEast.getType()==Material.WOOL)
            upnorthEast.setData(color);
        if (upnorthWest.getType()==Material.WOOL)
            upnorthWest.setData(color);
        if (upsouth.getType()==Material.WOOL)
            upsouth.setData(color);
        if (upsouthEast.getType()==Material.WOOL)
            upsouthEast.setData(color);
        if (upwest.getType()==Material.WOOL)
            upwest.setData(color);
        if (upsouthWest.getType()==Material.WOOL)
            upsouthWest.setData(color);

        if (downeast.getType()==Material.WOOL)
            downeast.setData(color);
        if (downnorth.getType()==Material.WOOL)
            downnorth.setData(color);
        if (downnorthEast.getType()==Material.WOOL)
            downnorthEast.setData(color);
        if (downnorthWest.getType()==Material.WOOL)
            downnorthWest.setData(color);
        if (downsouth.getType()==Material.WOOL)
            downsouth.setData(color);
        if (downsouthEast.getType()==Material.WOOL)
            downsouthEast.setData(color);
        if (downwest.getType()==Material.WOOL)
            downwest.setData(color);
        if (downsouthWest.getType()==Material.WOOL)
            downsouthWest.setData(color);

    }

}
