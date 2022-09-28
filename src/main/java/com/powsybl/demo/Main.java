/**
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.demo;

import com.powsybl.iidm.modification.NetworkModification;
import com.powsybl.iidm.modification.topology.CreateCouplingDeviceBuilder;
import com.powsybl.iidm.modification.topology.CreateVoltageLevelTopologyBuilder;
import com.powsybl.iidm.network.Network;
import com.powsybl.iidm.network.SwitchKind;
import com.powsybl.iidm.network.TopologyKind;
import com.powsybl.iidm.network.test.FictitiousSwitchFactory;
import com.powsybl.sld.SingleLineDiagram;

import java.nio.file.Paths;

/**
 * @author Miora Vedelago <miora.ralambotiana at rte-france.com>
 */
public class Main {

    public static void main(String[] args) {
        Network network = FictitiousSwitchFactory.create();

        // Creation et visualisation d'un poste vide
        network.getSubstation("A").newVoltageLevel().setNominalV(225).setTopologyKind(TopologyKind.NODE_BREAKER).setId("VL").add();
        SingleLineDiagram.draw(network, "VL", Paths.get("/tmp/VL0.svg"));

        // Creation et visualisation d'une topologie matricielle (4 jeux de barres et 3 sections)
        NetworkModification createTopo = new CreateVoltageLevelTopologyBuilder()
                .withBusbarCount(4)
                .withSectionCount(3)
                .withBusbarSectionPrefixId("VLbbs")
                .withSwitchPrefixId("VLsw")
                .withVoltageLevelId("VL")
                .withSwitchKinds(SwitchKind.DISCONNECTOR, SwitchKind.BREAKER)
                .build();
        createTopo.apply(network);
        SingleLineDiagram.draw(network, "VL", Paths.get("/tmp/VL1.svg"));

        // Creation et visualisation d'un omnibus
        NetworkModification createOmnibus = new CreateCouplingDeviceBuilder()
                .withBusbarSectionId1("VLbbs11")
                .withBusbarSectionId2("VLbbs32")
                .build();
        createOmnibus.apply(network);
        SingleLineDiagram.draw(network, "VL", Paths.get("/tmp/VL2.svg"));

        // Creation et visualisation d'un coupleur
        NetworkModification createCoupler = new CreateCouplingDeviceBuilder()
                .withBusbarSectionId1("VLbbs13")
                .withBusbarSectionId2("VLbbs23")
                .build();
        createCoupler.apply(network);
        SingleLineDiagram.draw(network, "VL", Paths.get("/tmp/VL3.svg"));
    }
}
