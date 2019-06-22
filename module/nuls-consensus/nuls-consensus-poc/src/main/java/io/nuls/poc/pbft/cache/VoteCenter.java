package io.nuls.poc.pbft.cache;

import io.nuls.base.data.NulsHash;
import io.nuls.poc.pbft.message.VoteMessage;
import io.nuls.poc.pbft.model.PbftData;
import io.nuls.poc.pbft.model.VoteData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Niels
 */
public class VoteCenter {

    private final int chainId;

    private Map<String, PbftData> map = new HashMap<>();

    public VoteCenter(int chainId) {
        this.chainId = chainId;
    }

    public PbftData addVote1(long height, int round, NulsHash hash, byte[] address, long startTime) {

        PbftData pbftData = getPbftData(height, round, startTime);


        //todo 收集恶意数据


        VoteData data = new VoteData();
        data.setAddress(address);
        data.setHash(hash);
        data.setHeight(height);
        data.setRound(round);
        pbftData.addVote1Result(data);


        return pbftData;
    }

    public PbftData addVote2(long height, int round, NulsHash hash, byte[] address, long startTime) {

        PbftData pbftData = getPbftData(height, round, startTime);

        //todo 收集恶意数据


        VoteData data = new VoteData();
        data.setAddress(address);
        data.setHash(hash);
        data.setHeight(height);
        data.setRound(round);
        pbftData.addVote2Result(data);
        return pbftData;
    }

    private PbftData getPbftData(long height, int round, long startTime) {
        String key = height + "_" + round;
        PbftData data = map.get(key);
        if (null == data) {
            data = new PbftData();
            data.setHeight(height);
            data.setRound(round);
            data.setStartTime(startTime);
            data.setEndTime(startTime + 10);
            map.put(key, data);
        }
        return data;
    }

    public boolean contains(VoteMessage message, byte[] address) {
        String key = message.getRound() + "_" + message.getRound();
        PbftData pbftData = map.get(key);
        if (null == pbftData) {
            return false;
        }
        if (message.getStep() == 0) {
            return null != pbftData.getVote1ByAddress(address);
        } else if (message.getStep() == 1) {
            return null != pbftData.getVote2ByAddress(address);
        }
        return false;
    }

    public PbftData getCurrentResult(long height, int round) {
        String key = height + "_" + round;
        return map.get(key);
    }
}
