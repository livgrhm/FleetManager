package fleetmanager.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Object that represents a Berlin district
 * Class variables:
 * idx         - an index to easily refer to a district,
 * numScooters - number of scooters available in the district
 * unitsWasted - if the FM attends this district, this is the number of
 *               extra scooter units that the FE's could have attended 
 *               (but can't, as they can attend one district only)
 * 
 * @author oliviagraham
 */
public class District {
    
    // auto-increment index (must be static so all instances share var)
    private static final AtomicInteger count = new AtomicInteger(0); 
    
    private final int idx;
    private final double numScooters;
    private int unitsWasted;
        
    public District (double numScooters) {
        this.idx = count.incrementAndGet();
        this.numScooters = numScooters;
    }
    
    @Override
    public String toString () {
        return  this.getIdx() + " " + 
                this.getNumScooters() + " " + 
                this.getUnitsWasted();
    }
    
    /**
     * @return the index (auto-increments)
     */
    public int getIdx() {
        return idx;
    }

    /**
     * @return the numScooters
     */
    public double getNumScooters() {
        return numScooters;
    }

    /**
     * @return the unitsWasted
     */
    public int getUnitsWasted() {
        return unitsWasted;
    }

    /**
     * @param unitsWasted the unitsWasted to set
     */
    public void setUnitsWasted(int unitsWasted) {
        this.unitsWasted = unitsWasted;
    }
    
    /**
     * Reset auto-increment between requests
     */
    public void resetAutoIncrement() {
        count.set(0);
    }
}
