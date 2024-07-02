package org.dreambot.behaviour.disablesettings;


import org.dreambot.MinnowsAIO;
import org.dreambot.framework.Branch;
import org.dreambot.framework.Leaf;

public class DisableSettingsBranch extends Branch<MinnowsAIO> {
    @Override
    public boolean isValid() {
        return children.stream().anyMatch(Leaf::isValid);
    }
}