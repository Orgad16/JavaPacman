package group23.pacman.system;

import java.io.File;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public interface AssetProtocol {

    /**
     * @return The name of the asset(s).
     */
    String name();

    /**
     * This method is used to check if the assets exist or not.
     * @param directory The directory in which the asset should exist.
     * @return true if the assets exist and false otherwise.
     */
    boolean checkIfExist(File directory);

    /**
     * This method is called if the files were not found and it's time to create them.
     * @param directory Is the directory in which the assets will be created.
     */
    void createDefaultFiles(File directory);
}
