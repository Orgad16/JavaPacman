package group23.pacman.system.protocols;

import group23.pacman.system.AssetProtocol;

import java.io.File;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class MapsAssetProtocol implements AssetProtocol{
    @Override
    public String name() {
        return "Maps";
    }

    @Override
    public boolean checkIfExist(File directory) {
        return false;
    }

    @Override
    public void createDefaultFiles(File directory) {

    }
}
