package org.tal.basiccircuits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tal.redstonechips.circuit.Circuit;
import org.tal.redstonechips.circuit.rcTypeReceiver;
import org.tal.redstonechips.util.BitSetUtils;



/**
 *
 * @author Tal Eisenberg
 */
public class print extends Circuit implements rcTypeReceiver {
    private final static int clockPin = 0;
    private final static int scrollPin = 2;
    private final static int clearPin = 1;

    enum Type {
        num, signed, unsigned, ascii, hex, oct, bin;
    }

    enum Display {
        replace, add, scroll
    }

    private Type type = Type.num;
    private Display display = Display.replace;

    private int dataPin = 1;
    private String[] lines = new String[4];
    private StringBuffer textBuffer = new StringBuffer();
    private Location[] blocksToUpdate;
    private SignUpdateTask signUpdateTask;

    private static final int LineSize = 15;

    int scrollPos = 0;

    @Override
    public void inputChange(int inIdx, boolean state) {
        if (inIdx==clockPin && state) {
            updateText(convertInputs());
            updateSigns();
        } else if (inIdx==clearPin && state && (display==Display.scroll || display==Display.add)) {
            clearSign();
        } else if (inIdx==scrollPin && state && display==Display.scroll) {
            if (scrollPos>=textBuffer.length()-1)
                scrollPos = 0;
            else
                scrollPos++;
            
            prepScrollLines();

            updateSigns();
        }
    }

    private void updateText(String text) {
        if (display==Display.add) {
            add(text);
            prepWrapLines();
            if (textBuffer.length()>LineSize*4) textBuffer.setLength(0);
        } else if (display==Display.replace) {
            textBuffer.setLength(0);
            textBuffer.append(text);
            prepWrapLines();
        } else if (display==Display.scroll) {
            add(text);
            prepScrollLines();
        }
    }

    private String convertInputs() {
        String text = "";

        if (type==Type.num || type==Type.unsigned) {
            text = Integer.toString(BitSetUtils.bitSetToUnsignedInt(inputBits, dataPin, inputs.length-dataPin));
        } else if (type==Type.signed) {
            text = Integer.toString(BitSetUtils.bitSetToSignedInt(inputBits, dataPin, inputs.length-dataPin));
        } else if (type==Type.hex) {
            text = Integer.toHexString(BitSetUtils.bitSetToUnsignedInt(inputBits, dataPin, inputs.length-dataPin));
        } else if (type==Type.oct) {
            text = Integer.toOctalString(BitSetUtils.bitSetToUnsignedInt(inputBits, dataPin, inputs.length-dataPin));
        } else if (type==Type.bin) {
            text = BitSetUtils.bitSetToBinaryString(inputBits, dataPin, inputs.length-dataPin);
        } else if (type==Type.ascii) {
            text = "" + (char)BitSetUtils.bitSetToUnsignedInt(inputBits, dataPin, inputs.length-dataPin);
        }

        return text;
    }

    private void add(String text) {
        if (type==Type.ascii || textBuffer.length()==0) {
            textBuffer.append(text);
        } else
            textBuffer.append(" ").append(text);
    }

    private void updateSigns() {
        if (hasDebuggers()) {
            debug("printing:");
            debug(lines[0]);
            debug(lines[1]);
            debug(lines[2]);
            debug(lines[3]);
        }

        redstoneChips.getServer().getScheduler().scheduleSyncDelayedTask(redstoneChips, signUpdateTask);
    }

    private void prepWrapLines() {
        if (textBuffer.length()>LineSize*3) {
            String line4 = textBuffer.substring(LineSize*3);
            if (line4.length()>LineSize) line4.substring(0, LineSize);

            lines[0] = textBuffer.substring(0, LineSize);
            lines[1] = textBuffer.substring(LineSize, LineSize*2);
            lines[2] = textBuffer.substring(LineSize*2, LineSize*3);
            lines[3] = line4;
        } else if (textBuffer.length()>LineSize*2) {
            lines[0] = "";
            lines[1] = textBuffer.substring(0, LineSize);
            lines[2] = textBuffer.substring(LineSize, LineSize*2);
            lines[3] = textBuffer.substring(LineSize*2);
        } else if (textBuffer.length()>LineSize) {
            lines[0] = "";
            lines[1] = textBuffer.substring(0,LineSize);
            lines[2] = textBuffer.substring(LineSize);
            lines[3] = "";
        } else {
            lines[0] = "";
            lines[1] = textBuffer.toString();
            lines[2] = "";
            lines[3] = "";
        }
    }

    private void prepScrollLines() {
        String window;

        if (textBuffer.length()>LineSize) { // turn scrolling on
            int end = Math.min(scrollPos+LineSize, textBuffer.length());
            window = textBuffer.substring(scrollPos, end);
            if (window.length()<LineSize) {
                window += " " + textBuffer.substring(0, LineSize-window.length());
            }
        } else window = textBuffer.toString();

        lines[0] = "";
        lines[1] = window;
        lines[2] = "";
        lines[3] = "";
    }

    private void clearSign() {
        if (hasDebuggers()) debug("Clearing sign.");
        textBuffer.setLength(0);
        scrollPos = 0;
        lines[0] = "";
        lines[1] = "";
        lines[2] = "";
        lines[3] = "";
        updateSigns();
    }

    @Override
    public boolean init(CommandSender sender, String[] args) {
        if (args.length>0) {
            try {
                type = Type.valueOf(args[0]);
            } catch (IllegalArgumentException ie) {
                error(sender, "Unknown type: " + args[0]);
                return false;
            }

            if (args.length>1) {
                try {
                    display = Display.valueOf(args[1]);
                } catch (IllegalArgumentException ie) {
                    error(sender, "Unknown display arg: " + args[1]);
                    return false;
                }
            }

        }

        if (display==Display.add && inputs.length<2) {
            error(sender, "Expecting at least 2 inputs. 1 clock pin and 1 data pin.");
            return false;
        } else if (display==Display.replace && inputs.length<3) {
            error(sender, "Expecting at least 3 inputs. 1 clock pin, 1 clear pin and 1 data pin.");
            return false;
        } else if (display==Display.scroll && inputs.length<4) {
            error(sender, "Expecting at least 4 inputs. 1 clock pin, 1 clear pin, 1 scroll pin and 1 data pin.");
            return false;
        }

        if (interfaceBlocks.length==0) {
            error(sender, "Expecting at least 1 interaction block.");
            return false;
        }

        List<Location> blockList = new ArrayList<Location>();
        for (Location l : interfaceBlocks) {
            Block i = world.getBlockAt(l);
            Block north = i.getFace(BlockFace.NORTH);
            Block south = i.getFace(BlockFace.SOUTH);
            Block west = i.getFace(BlockFace.WEST);
            Block east = i.getFace(BlockFace.EAST);
            if (!isStructureBlock(north)) blockList.add(north.getLocation());
            if (!isStructureBlock(south)) blockList.add(south.getLocation());
            if (!isStructureBlock(west)) blockList.add(west.getLocation());
            if (!isStructureBlock(east)) blockList.add(east.getLocation());
        }

        this.blocksToUpdate = blockList.toArray(new Location[blockList.size()]);

        signUpdateTask = new SignUpdateTask(blocksToUpdate);

        if (display==Display.replace) dataPin = 1;
        else if (display==Display.add) dataPin = 2;
        else if (display==Display.scroll) dataPin = 3;

        redstoneChips.registerRcTypeReceiver(activationBlock, this);



        return true;
    }

    private boolean isStructureBlock(Block b) {
        for (Location s : structure)
            if (b.getLocation().equals(s)) return true;

        return false;
    }

    @Override
    public void setInternalState(Map<String, String> state) {
        String text = state.get("text");
        if (text!=null) {
            textBuffer.setLength(0);
            textBuffer.append(text);
        }
    }

    @Override
    public Map<String, String> getInternalState() {
        Map<String,String> state = new HashMap<String,String>();
        state.put("text", textBuffer.toString());
        return state;
    }


    class SignUpdateTask implements Runnable {
        Sign[] signList;
        int curSign = 0;
        long lastRunTime = -1;

        @Override
        public void run() {
            //if (world.getTime()==lastRunTime) return;
            Sign s = signList[curSign];
            s.setLine(0, lines[0]);
            s.setLine(1, lines[1]);
            s.setLine(2, lines[2]);
            s.setLine(3, lines[3]);
            s.update();
            

            lastRunTime = world.getTime();
            if (curSign<signList.length-1) {
                curSign++;

                redstoneChips.getServer().getScheduler().scheduleSyncDelayedTask(redstoneChips, signUpdateTask, 1);
            } else curSign=0;

        }

        public SignUpdateTask(Location[] blocksToUpdate) throws IllegalArgumentException {
            List<Sign> list = new ArrayList<Sign>();

            for (Location l : blocksToUpdate) {
                if (world.getBlockTypeIdAt(l.getBlockX(), l.getBlockY(), l.getBlockZ())==Material.WALL_SIGN.getId()) {
                    list.add((Sign)world.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).getState());
                }
            }

            signList = list.toArray(new Sign[list.size()]);
        }
    }

    @Override
    public void type(String[] words, Player player) {
        if (words.length==0) return;

        String text = "";
        for (String word : words)
            text += word + " ";
        updateText(text.substring(0, text.length()-1));
        updateSigns();
    }


}
