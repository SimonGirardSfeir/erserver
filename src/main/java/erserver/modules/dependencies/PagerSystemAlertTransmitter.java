package erserver.modules.dependencies;

import erserver.modules.dependencies.vendorpagersystem.PagerSystem;
import erserver.modules.dependencies.vendorpagersystem.PagerTransport;

public class PagerSystemAlertTransmitter implements AlertTransmitter {
    @Override
    public void transmit(String targetDevice, String pageText) {
        PagerTransport transport = getInitializedTransport();
        transport.transmit(targetDevice, pageText);
    }

    @Override
    public void transmitRequiringAcknowledgement(String targetDevice, String pageText) {
        PagerTransport transport = getInitializedTransport();
        transport.transmitRequiringAcknowledgement(targetDevice, pageText);
    }

    private static PagerTransport getInitializedTransport() {
        PagerTransport transport = PagerSystem.getTransport();
        transport.initialize();
        return transport;
    }
}
