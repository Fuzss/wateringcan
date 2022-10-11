package fuzs.wateringcan.world.level.block;

import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Iterator;
import java.util.List;

public class SpongeScheduler {
    public static final SpongeScheduler INSTANCE = new SpongeScheduler();

    private final List<AbstractSpongeTask> tasks = Lists.newArrayList();

    public void scheduleTask(AbstractSpongeTask task) {
        this.tasks.add(task);
    }

    public void onServerWorld$Tick(ServerLevel level) {
        Iterator<AbstractSpongeTask> iterator = this.tasks.iterator();
        while (iterator.hasNext()) {
            AbstractSpongeTask task = iterator.next();
            if (task.mayAdvance(level)) {
                if (task.advance(3)) {
                    iterator.remove();
                }
            }
        }
    }

    public void onServerWorld$Unload(ServerLevel level) {
        Iterator<AbstractSpongeTask> iterator = this.tasks.iterator();
        while (iterator.hasNext()) {
            AbstractSpongeTask task = iterator.next();
            task.finishQuickly();
            iterator.remove();
        }
    }

    public void onChunk$Unload(ChunkAccess chunk) {
        if (chunk instanceof LevelChunk levelChunk) {
            Iterator<AbstractSpongeTask> iterator = this.tasks.iterator();
            while (iterator.hasNext()) {
                AbstractSpongeTask task = iterator.next();
                if (task.mayAdvance(levelChunk.getLevel())) {
                    ChunkPos chunkPos = levelChunk.getPos();
                    if (task.containsBlocksAtChunkPos(chunkPos.x, chunkPos.z)) {
                        task.finishQuickly();
                        iterator.remove();
                    }
                }
            }
        }
    }
}
