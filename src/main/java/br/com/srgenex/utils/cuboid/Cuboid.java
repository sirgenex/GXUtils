package br.com.srgenex.utils.cuboid;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

@Getter
public class Cuboid implements Iterable<Block>{

    private World world;
    private int xmax;
    private int xmin;
    private int ymax;
    private int ymin;
    private int zmax;
    private int zmin;

    public Cuboid(Location l, int radius){
        Location l1 = l.clone().add(-radius, radius, radius);
        Location l2 = l.clone().add(radius, -radius, -radius);
        if(l1.getWorld().getName().equals(l2.getWorld().getName())){
            this.world = l1.getWorld();
            this.xmax = Math.max(l1.getBlockX(), l2.getBlockX());
            this.xmin = Math.min(l1.getBlockX(), l2.getBlockX());
            this.ymax = Math.max(l1.getBlockY(), l2.getBlockY());
            this.ymin = Math.min(l1.getBlockY(), l2.getBlockY());
            this.zmax = Math.max(l1.getBlockZ(), l2.getBlockZ());
            this.zmin = Math.min(l1.getBlockZ(), l2.getBlockZ());
        }
    }

    public Cuboid(int xmax, int xmin, int ymax, int ymin, int zmax, int zmin, World world){
        this.world = world;
        this.xmax = xmax;
        this.xmin = xmin;
        this.ymax = ymax;
        this.ymin = ymin;
        this.zmax = zmax;
        this.zmin = zmin;
    }

    @Override
    public Iterator<Block> iterator() {
        return new CuboidIterator(new Cuboid(xmax, xmin, ymax, ymin, zmax, zmin, world));
    }

    public boolean contains(int x, int y, int z) {
        return x >= this.xmin && x <= this.xmax && y >= this.ymin && y <= this.ymax && z >= this.zmin && z <= this.zmax;
    }

    public class CuboidIterator implements Iterator<Block>{

        Cuboid cci;
        World wci;
        int baseX;
        int baseY;
        int baseZ;
        int sizeX;
        int sizeY;
        int sizeZ;
        private int x, y, z;

        public CuboidIterator(Cuboid c){
            this.cci = c;
            this.wci = c.getWorld();
            baseX = getXmin();
            baseY = getYmin();
            baseZ = getZmin();
            sizeX = Math.abs(getXmax() - getXmin()) + 1;
            sizeY = Math.abs(getYmax() - getYmin()) + 1;
            sizeZ = Math.abs(getZmax() - getZmin()) + 1;
            x = y = z = 0;
        }

        public boolean hasNext() {
            return x < sizeX && y < sizeY && z < sizeZ;
        }

        public Block next() {
            Block b = getWorld().getBlockAt(baseX + x, baseY + y, baseZ + z);
            if (++x >= sizeX) {
                x = 0;
                if (++y >= sizeY) {
                    y = 0;
                    ++z;
                }
            }
            return b;
        }

        public void remove() {}

    }

}