package su.nightexpress.excellentjobs.contract.condition.impl;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentjobs.contract.ContractManager;
import su.nightexpress.excellentjobs.contract.condition.UnlockCondition;
import su.nightexpress.excellentjobs.contract.data.ContractData;
import su.nightexpress.excellentjobs.contract.model.Contract;
import su.nightexpress.excellentjobs.job.JobManager;
import su.nightexpress.excellentjobs.job.model.Job;
import su.nightexpress.excellentjobs.job.model.JobInfo;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Numbers;

@NullMarked
public class ActiveContractsCondition implements UnlockCondition {

    public static final String NAME = "active_contracts";

    private final JobManager      jobManager;
    private final ContractManager contractManager;

    private final Map<String, int[]> contractAmounts;

    public ActiveContractsCondition(JobManager jobManager,
                                    ContractManager contractManager,
                                    Map<String, int[]> contractAmounts) {
        this.jobManager = jobManager;
        this.contractManager = contractManager;
        this.contractAmounts = contractAmounts;
    }

    @Override
    public boolean check(Player player, Contract contract, Job job) {
        for (var entry : contractAmounts.entrySet()) {
            String contractId = entry.getKey();
            int[] minMax = entry.getValue();

            int min = minMax[0];
            int max = minMax[1];

            int jobsWithContract = this.countJobsWithContract(player, contractId);

            if (jobsWithContract < min) return false;
            if (jobsWithContract > max && max != -1) return false;
        }

        return true;
    }

    private int countJobsWithContract(Player player, String contractId) {
        int jobsWithContract = 0;

        for (JobInfo jobInfo : this.jobManager.getActiveJobs(player)) {
            ContractData contractData = this.contractManager.getContractData(player, jobInfo.job());
            if (contractData == null) continue;

            if (contractData.getContractId().equalsIgnoreCase(contractId)) {
                jobsWithContract++;
            }
        }

        return jobsWithContract;
    }

    public static class Codec implements ConfigCodec<ActiveContractsCondition> {

        private final JobManager      jobManager;
        private final ContractManager contractManager;

        public Codec(JobManager jobManager, ContractManager contractManager) {
            this.jobManager = jobManager;
            this.contractManager = contractManager;
        }

        @Override
        public ActiveContractsCondition read(FileConfig config, String path) throws CodecReadException {
            Map<String, int[]> contractAmounts = parseAmounts(config, path);

            return new ActiveContractsCondition(this.jobManager, this.contractManager, contractAmounts);
        }

        @Override
        public void write(FileConfig config, String path, ActiveContractsCondition value) {
            config.remove(path);
            value.contractAmounts.forEach((contractId, minMax) -> {
                config.set(path + "." + contractId, "%s-%s".formatted(minMax[0], minMax[1]));
            });
        }

        private static Map<String, int[]> parseAmounts(FileConfig config, String path) {
            Map<String, int[]> contractAmounts = new HashMap<>();
            config.getSection(path).forEach(contractId -> {
                String raw = config.getString(path + "." + contractId);
                if (raw == null) return;

                String[] split = raw.trim().split("-");

                int min = Numbers.getIntegerAbs(split[0]);
                int max = split.length >= 2 ? Numbers.getIntegerAbs(split[1]) : -1;

                contractAmounts.put(contractId, new int[]{min, max});
            });
            return contractAmounts;
        }
    }
}
