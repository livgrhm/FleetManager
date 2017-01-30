package fleetmanager.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Fleet Configuration
 * 
 * Takes the input from HTTP params; scooters, FM capacity, FE capacity
 * Provides methods for fleet management
 * 
 * @author oliviagraham
 */
public class FleetConfig {
    
    // Input variables, from params
    private final double[] scooterList;
    private final int fleetManagerCapacity;
    private final int fleetEngineerCapacity;
    
    // Output variables
    private int totalEngineersRequired;
    
    // Helpers
    private ArrayList<District> fleetDistricts;
    
    // Constructor
    public FleetConfig (double[] scooterList, int fleetManagerCapacity, 
            int fleetEngineerCapacity) {
        
        this.scooterList = scooterList;
        this.fleetManagerCapacity = fleetManagerCapacity;
        this.fleetEngineerCapacity = fleetEngineerCapacity;
        this.totalEngineersRequired = 0;
    }
    
    /**
     * @deprecated 
     * 
     * Performs calculations to find minimum number of FE's required to 
     * manage the fleet.
     * 
     * Works by selecting first district with minimum wastage
     * Does not account for multiple districts with same minimum wastage
     */
    public void calculateFleetEngineers() {
        if (fleetDistricts == null) {
            // Initialise Districts list
            this.getFleetDistricts();
        }
        
        // Select the most efficient district to send the FM to
        District fmDistrict = this.getFmDistrict().get(0);
        // Remove from list
        fleetDistricts.remove(0);
        
        System.out.println("\n**********\nCalculating Num FE's\n**********");
        System.out.println("\nFE: " + fleetEngineerCapacity + "\n**********");
        
        fleetDistricts.stream().forEach(d -> {
            // Calculate required FE's and wastage
            double required = Math.ceil(d.getNumScooters() / fleetEngineerCapacity);
            double wastage = (d.getNumScooters() % fleetEngineerCapacity);
            
            // Print calculations
            System.out.println("Num Scooters in District: " + d.getNumScooters());
            System.out.println("FE's required: " + required);
            System.out.println("Wastage: " + wastage);
            this.setTotalEngineersRequired(
                    this.getTotalEngineersRequired() + (int)required);
            System.out.println("==========");
        });
        
        // Take into account FE's needed to help the FM in their district
        double x = (fmDistrict.getNumScooters() - fleetManagerCapacity); 
        int feHelpers = (int)Math.ceil(x / fleetEngineerCapacity);
        this.setTotalEngineersRequired(this.getTotalEngineersRequired() + feHelpers);
        
        // Reset Districts auto-increment for future requests
        if (fleetDistricts.size() > 0) {
            fleetDistricts.get(0).resetAutoIncrement();
        }
    }
    
    /**
     * Performs calculations to find minimum number of FE's required to 
     * manage the fleet.
     * 
     * V2: takes into account a case where multiple districts have minimum
     * wastage; checks each case to find most efficient
     * 
     */
    public void calculateFleetEngineersv2() {
        if (fleetDistricts == null) {
            // Initialise Districts list
            this.getFleetDistricts();
        }

        // Get all the minimum wastage districts
        ArrayList<District> fmDistricts = this.getFmDistrict();
        ArrayList<Integer> totalEng = new ArrayList<>();
        
        System.out.println("\n**********\nCalculating Num FE's\n**********");
        System.out.println("\nFE: " + fleetEngineerCapacity + "\n**********");
        
        // Loop over potential FM districts
        fmDistricts.stream().forEach(fmDist -> {
            
            District fmDistrict = fmDist;
            int totalEngDist = 0;
            
            System.out.println("Calculation for district: " + fmDistrict.toString());
            
            // Loop over all other districts, except current FM district
            for (int x = 0; x < fleetDistricts.size(); x++) {
                
                District feDistrict = fleetDistricts.get(x);
                
                if (feDistrict.getIdx() != fmDistrict.getIdx()) {
                    // Calculate required FE's + add to total
                    double required = Math.ceil(feDistrict.getNumScooters() / fleetEngineerCapacity);
                    totalEngDist = totalEngDist + (int)required;
                }
            }

            // Take into account FE's needed to help the FM in their district
            double x = (fmDistrict.getNumScooters() - fleetManagerCapacity); 
            int feHelpers = (int)Math.ceil(x / fleetEngineerCapacity);
            int totalEngDistFinal = totalEngDist + feHelpers;
            
            // Store the total FE's required for this district
            totalEng.add(totalEngDistFinal);
            
            System.out.println("Total FE's if FM attends this district: " + totalEngDistFinal);
            System.out.println("========");
        });
        
        // Order by min engineers required, select smallest (least wastage) 
        Collections.sort(totalEng);
        this.setTotalEngineersRequired(totalEng.get(0));
        
        System.out.println("\n**********\nFinal FE Calculation\n**********");
        System.out.println(this.getTotalEngineersRequired());
        
        // Reset Districts auto-increment for future requests
        if (fleetDistricts.size() > 0) {
            fleetDistricts.get(0).resetAutoIncrement();
        }
    }
    
    /**
     * Get Fleet Districts
     * Initial call creates District objects
     * and calculates wastage for if the FM attends each district
     * 
     * @return the fleetDistricts
     */
    public ArrayList<District> getFleetDistricts() {
        if (fleetDistricts == null) {
            
            fleetDistricts = new ArrayList<>();
            
            System.out.println("\n**********\nBuilding Districts\n**********");
            System.out.println("\nFM: " + fleetManagerCapacity + 
                    ", FE: " + fleetEngineerCapacity + "\n**********");
            
            // Build list of Districts
            Arrays.stream(scooterList).forEach(district -> {

                // Create district Object
                District d = new District(district);

                // remainder after removing fleet mgr capacity
                double rem = d.getNumScooters() - fleetManagerCapacity;
                
                // multiplier for FE's needed to cover remainder
                double ceiling = Math.ceil(rem / fleetEngineerCapacity);
                
                // num units wasted if FM attends this district
                // if remainder is negative, wastage is equal to remainder
                double wasted;
                if (rem < 0) {
                    wasted = Math.abs(rem);
                    ceiling = 0;
                } else {
                    wasted = (ceiling * fleetEngineerCapacity) - rem;
                }
                d.setUnitsWasted((int) wasted);

                // Print calculations
                System.out.println("Num Scooters in District: " + d.getNumScooters());
                System.out.println("Remainder if district chosen by FM: " + rem);
                System.out.println("Required FE to cover remainder: " + ceiling);
                System.out.println("Units wasted after FE coverage: " + wasted);
                System.out.println("==========");

                // Push district on to ArrayList
                fleetDistricts.add(d);
            });
        }
        
        return fleetDistricts;
    }

    /**
     * Find the best district to send the FM to
     * 
     * @return fmDistrict List of District objects
     */
    public ArrayList<District> getFmDistrict () {
        if (fleetDistricts == null) {
            return null;
        }
        
        // sort the Districts by wastage & select smallest as the FM's district
        Collections.sort(fleetDistricts, 
                (d1, d2) -> d1.getUnitsWasted() >= d2.getUnitsWasted() ? 0 : -1);
        
        // Print sort result (for debug/validation)
        System.out.println("\n**********\nFinding FM District\n**********");
        fleetDistricts.stream().forEach(d -> System.out.println(d.toString()));

        // while wastage is same as fleetDistricts.get(0), push option onto array
        // this is where more than one district has the lowest wastage
        ArrayList<District> fmDistricts = new ArrayList<>();
        District firstDist = fleetDistricts.get(0);
        fmDistricts.add(firstDist);
        int count = 1;
        while ((count < fleetDistricts.size())
                && fleetDistricts.get(count).getUnitsWasted() 
                == firstDist.getUnitsWasted()) {
            
            fmDistricts.add(fleetDistricts.get(count));
            count++;
        }
        
        // Print chosen district(s)
        System.out.println("========\nDistricts(s) with least wastage: ");
        fmDistricts.stream().forEach(d -> System.out.println(d.toString()));
        System.out.println("========");
        return fmDistricts;
    }

    /**
     * @return the totalEngineersRequired
     */
    public int getTotalEngineersRequired() {
        return totalEngineersRequired;
    }

    /**
     * @param totalEngineersRequired the totalEngineersRequired to set
     */
    public void setTotalEngineersRequired(int totalEngineersRequired) {
        this.totalEngineersRequired = totalEngineersRequired;
    }
}
