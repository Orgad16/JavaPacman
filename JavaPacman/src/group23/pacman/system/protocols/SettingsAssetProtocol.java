package group23.pacman.system.protocols;

import group23.pacman.system.AssetProtocol;

import java.io.File;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class SettingsAssetProtocol implements AssetProtocol{
    @Override
    public String name() {
        return "Settings";
    }

    @Override
    public boolean checkIfExist(File directory) {
        return false;
    }

    @Override
    public void createDefaultFiles(File directory) {

    }
}