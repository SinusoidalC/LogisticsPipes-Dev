package logisticspipes.network.packets.module;

import java.io.IOException;

import logisticspipes.modules.ModuleModBasedItemSink;
import logisticspipes.network.LPDataInputStream;
import logisticspipes.network.LPDataOutputStream;
import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.network.abstractpackets.NBTCoordinatesPacket;
import logisticspipes.pipes.PipeLogisticsChassi;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.gui.DummyModuleContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.player.EntityPlayer;

@Accessors(chain=true)
public class ModuleBasedItemSinkList extends NBTCoordinatesPacket {

	@Getter
	@Setter
	private int slot;
	
	public ModuleBasedItemSinkList(int id) {
		super(id);
	}

	@Override
	public ModernPacket template() {
		return new ModuleBasedItemSinkList(getId());
	}

	@Override
	public void processPacket(EntityPlayer player) {
		if(MainProxy.isClient(player.worldObj)) {
			final LogisticsTileGenericPipe pipe = this.getPipe(player.worldObj);
			if(pipe == null) {
				return;
			}
			if(pipe.pipe instanceof PipeLogisticsChassi && ((PipeLogisticsChassi)pipe.pipe).getModules() != null && ((PipeLogisticsChassi)pipe.pipe).getModules().getSubModule(getSlot()) instanceof ModuleModBasedItemSink) {
				((ModuleModBasedItemSink)((PipeLogisticsChassi)pipe.pipe).getModules().getSubModule(getSlot())).readFromNBT(getTag());
			}
		} else {
			if(getSlot() < 0) {
				if(player.openContainer instanceof DummyModuleContainer) {
					DummyModuleContainer dummy = (DummyModuleContainer) player.openContainer;
					if(dummy.getModule() instanceof ModuleModBasedItemSink) {
						((ModuleModBasedItemSink)dummy.getModule()).readFromNBT(getTag());
						return;
					}
				}
			}
			final LogisticsTileGenericPipe pipe = this.getPipe(player.worldObj);
			if(pipe == null) {
				return;
			}
			if(pipe.pipe instanceof PipeLogisticsChassi && ((PipeLogisticsChassi)pipe.pipe).getModules() != null && ((PipeLogisticsChassi)pipe.pipe).getModules().getSubModule(getSlot()) instanceof ModuleModBasedItemSink) {
				((ModuleModBasedItemSink)((PipeLogisticsChassi)pipe.pipe).getModules().getSubModule(getSlot())).readFromNBT(getTag());
				((ModuleModBasedItemSink)((PipeLogisticsChassi)pipe.pipe).getModules().getSubModule(getSlot())).ModListChanged();
			}
		}
	}

	@Override
	public void writeData(LPDataOutputStream data) throws IOException {
		super.writeData(data);
		data.writeInt(slot);
	}

	@Override
	public void readData(LPDataInputStream data) throws IOException {
		super.readData(data);
		slot = data.readInt();
	}
}

