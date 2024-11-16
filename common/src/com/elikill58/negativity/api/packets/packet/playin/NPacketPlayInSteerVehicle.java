package com.elikill58.negativity.api.packets.packet.playin;

import com.elikill58.negativity.api.packets.PacketType;
import com.elikill58.negativity.api.packets.nms.PacketSerializer;
import com.elikill58.negativity.api.packets.packet.NPacketPlayIn;
import com.elikill58.negativity.universal.Version;

public class NPacketPlayInSteerVehicle implements NPacketPlayIn {

    private static final float IMPULSE = 0.98F;
    
	public float sideways = 0, forward = 0;
	public boolean jumping = false, sneaking = false;
	
	public NPacketPlayInSteerVehicle() {
		
	}
	
	public NPacketPlayInSteerVehicle(float sideways, float forward, boolean jumping, boolean sneaking) {
		this.sideways = sideways;
		this.forward = forward;
		this.jumping = jumping;
		this.sneaking = sneaking;
	}
	
	@Override
	public void read(PacketSerializer serializer, Version version) {
		if(version.isNewerOrEquals(Version.V1_21)) {

            byte flags = serializer.readByte();
            boolean left = (flags & 1 << 2) != 0;
            boolean right = (flags & 1 << 3) != 0;
            sideways = left ? IMPULSE : (right ? -IMPULSE : 0F);

            boolean forwards = (flags & 1 << 0) != 0;
            boolean backward = (flags & 1 << 1) != 0;
            forward = forwards ? IMPULSE : (backward ? -IMPULSE : 0F);

            byte updatedFlags = 0;
            // firstly convert content to actual data
            if ((flags & 1 << 4) != 0) {
                updatedFlags |= 1;
            }
            if ((flags & 1 << 5) != 0) {
                updatedFlags |= 2;
            }
            // then get them back
		    this.jumping = ((updatedFlags & 0x1) > 0);
		    this.sneaking = ((updatedFlags & 0x2) > 0);
		    // maybe this can be simplified ?
		} else {
		    this.sideways = serializer.readFloat();
		    this.forward = serializer.readFloat();
		    byte b = serializer.readByte();
		    this.jumping = ((b & 0x1) > 0);
		    this.sneaking = ((b & 0x2) > 0);
		}
	}
	
	@Override
	public PacketType getPacketType() {
		return PacketType.Client.STEER_VEHICLE;
	}
}
