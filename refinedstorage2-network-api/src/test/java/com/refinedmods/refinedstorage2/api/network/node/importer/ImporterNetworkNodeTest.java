package com.refinedmods.refinedstorage2.api.network.node.importer;

import com.refinedmods.refinedstorage2.api.core.Action;
import com.refinedmods.refinedstorage2.api.network.test.NetworkTestFixtures;
import com.refinedmods.refinedstorage2.api.network.test.extension.AddNetworkNode;
import com.refinedmods.refinedstorage2.api.network.test.extension.InjectNetworkStorageChannel;
import com.refinedmods.refinedstorage2.api.network.test.extension.NetworkTestExtension;
import com.refinedmods.refinedstorage2.api.network.test.extension.SetupNetwork;
import com.refinedmods.refinedstorage2.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage2.api.storage.Actor;
import com.refinedmods.refinedstorage2.api.storage.EmptyActor;
import com.refinedmods.refinedstorage2.api.storage.InMemoryStorageImpl;
import com.refinedmods.refinedstorage2.api.storage.channel.StorageChannel;
import com.refinedmods.refinedstorage2.api.storage.limited.LimitedStorageImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(NetworkTestExtension.class)
@SetupNetwork
class ImporterNetworkNodeTest {
    @AddNetworkNode(energyUsage = 5)
    ImporterNetworkNode sut;

    @Test
    void testInitialState() {
        // Assert
        assertThat(sut.getEnergyUsage()).isEqualTo(5);
    }

    @Test
    void testTransfer(@InjectNetworkStorageChannel final StorageChannel<String> storageChannel) {
        // Arrange
        storageChannel.addSource(new InMemoryStorageImpl<>());

        final FakeImporterSource source = new FakeImporterSource("A", "B")
            .add("A", 100)
            .add("B", 100);
        final ImporterTransferStrategy strategy = new ImporterTransferStrategyImpl<>(
            source,
            sut,
            NetworkTestFixtures.STORAGE_CHANNEL_TYPE,
            1
        );
        sut.setTransferStrategy(strategy);

        // Act
        sut.update();

        // Assert
        assertThat(storageChannel.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactly(
            new ResourceAmount<>("A", 1)
        );
        assertThat(source.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            new ResourceAmount<>("A", 99),
            new ResourceAmount<>("B", 100)
        );
    }

    @Test
    void testTransferWithoutSpaceInNetwork(
        @InjectNetworkStorageChannel final StorageChannel<String> storageChannel) {
        // Arrange
        storageChannel.addSource(new LimitedStorageImpl<>(100));
        storageChannel.insert("C", 100, Action.EXECUTE, EmptyActor.INSTANCE);

        final FakeImporterSource source = new FakeImporterSource("A", "B")
            .add("A", 100)
            .add("B", 100);
        final ImporterTransferStrategy strategy = new ImporterTransferStrategyImpl<>(
            source,
            sut,
            NetworkTestFixtures.STORAGE_CHANNEL_TYPE,
            1
        );
        sut.setTransferStrategy(strategy);

        // Act
        sut.update();

        // Assert
        assertThat(storageChannel.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactly(
            new ResourceAmount<>("C", 100)
        );
        assertThat(source.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            new ResourceAmount<>("A", 100),
            new ResourceAmount<>("B", 100)
        );
    }

    @Test
    void testCompleteTransferOverMultipleSlots(
        @InjectNetworkStorageChannel final StorageChannel<String> storageChannel) {
        // Arrange
        storageChannel.addSource(new InMemoryStorageImpl<>());

        final FakeImporterSource source = new FakeImporterSource("A", "B", "A", "B")
            .add("A", 10)
            .add("B", 6);
        final ImporterTransferStrategy strategy = new ImporterTransferStrategyImpl<>(
            source,
            sut,
            NetworkTestFixtures.STORAGE_CHANNEL_TYPE,
            10
        );
        sut.setTransferStrategy(strategy);

        // Act
        sut.update();

        // Assert
        assertThat(storageChannel.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactly(
            new ResourceAmount<>("A", 10)
        );
        assertThat(source.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            new ResourceAmount<>("B", 6)
        );
    }

    @Test
    void testTransferOverMultipleSlots(
        @InjectNetworkStorageChannel final StorageChannel<String> storageChannel) {
        // Arrange
        storageChannel.addSource(new InMemoryStorageImpl<>());

        final FakeImporterSource source = new FakeImporterSource("A", "B", "A", "B")
            .add("A", 12)
            .add("B", 6);
        final ImporterTransferStrategy strategy = new ImporterTransferStrategyImpl<>(
            source,
            sut,
            NetworkTestFixtures.STORAGE_CHANNEL_TYPE,
            10
        );
        sut.setTransferStrategy(strategy);

        // Act
        sut.update();

        // Assert
        assertThat(storageChannel.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactly(
            new ResourceAmount<>("A", 10)
        );
        assertThat(source.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            new ResourceAmount<>("A", 2),
            new ResourceAmount<>("B", 6)
        );
    }

    @Test
    void testTransferWhereResourceIsNotAccepted(
        @InjectNetworkStorageChannel final StorageChannel<String> storageChannel) {
        // Arrange
        storageChannel.addSource(new InMemoryStorageImpl<>() {
            @Override
            public long insert(final String resource, final long amount, final Action action, final Actor actor) {
                if ("A".equals(resource)) {
                    return 0;
                }
                return super.insert(resource, amount, action, actor);
            }
        });

        final FakeImporterSource source = new FakeImporterSource("A", "B", "B", "B")
            .add("A", 8)
            .add("B", 11);
        final ImporterTransferStrategy strategy = new ImporterTransferStrategyImpl<>(
            source,
            sut,
            NetworkTestFixtures.STORAGE_CHANNEL_TYPE,
            10
        );
        sut.setTransferStrategy(strategy);

        // Act
        sut.update();

        // Assert
        assertThat(storageChannel.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactly(
            new ResourceAmount<>("B", 10)
        );
        assertThat(source.getAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            new ResourceAmount<>("A", 8),
            new ResourceAmount<>("B", 1)
        );
    }
}
