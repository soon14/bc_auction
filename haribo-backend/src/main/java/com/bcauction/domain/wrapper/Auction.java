package com.bcauction.domain.wrapper;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */
public class Auction extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405160a0806106838339810160409081528151602083015191830151606084015160809094015160008054600160a060020a031916600160a060020a0390941693909317835560049390935560035560019290925560025561060990819061007a90396000f3006080604052600436106100c45763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166312fa6feb81146100c95780631998aeef146100f25780633ccfd60b146100fc5780634b449cba146101115780635b90dbc4146101385780636ba35e70146101595780638da5cb5b1461016e5780638fa8b7901461019f57806391f90157146101b4578063963e63c7146101c9578063d57bde79146101de578063eb54f9ec146101f3578063fe67a54b14610208575b600080fd5b3480156100d557600080fd5b506100de61021d565b604080519115158252519081900360200190f35b6100fa610226565b005b34801561010857600080fd5b506100de6102e9565b34801561011d57600080fd5b50610126610353565b60408051918252519081900360200190f35b34801561014457600080fd5b50610126600160a060020a0360043516610359565b34801561016557600080fd5b50610126610374565b34801561017a57600080fd5b5061018361037a565b60408051600160a060020a039092168252519081900360200190f35b3480156101ab57600080fd5b506100fa610389565b3480156101c057600080fd5b50610183610471565b3480156101d557600080fd5b50610126610480565b3480156101ea57600080fd5b50610126610486565b3480156101ff57600080fd5b5061012661048c565b34801561021457600080fd5b506100fa610492565b60095460ff1681565b600054600160a060020a031633141561023e57600080fd5b6006543411610297576040805160e560020a62461bcd02815260206004820152601e60248201527f546865726520616c7265616479206973206120686967686572206269642e0000604482015290519081900360640190fd5b600654156102c457600654600554600160a060020a03166000908152600760205260409020805490910190555b6005805473ffffffffffffffffffffffffffffffffffffffff19163317905534600655565b336000908152600760205260408120548181111561034a57336000818152600760205260408082208290555183156108fc0291849190818181858888f19350505050151561034a57336000908152600760205260408120829055915061034f565b600191505b5090565b60025481565b600160a060020a031660009081526007602052604090205490565b60045481565b600054600160a060020a031681565b6002544210156103e3576040805160e560020a62461bcd02815260206004820152601560248201527f41756374696f6e206e6f742079657420656e6465640000000000000000000000604482015290519081900360640190fd5b60095460ff161561043e576040805160e560020a62461bcd02815260206004820152601f60248201527f61756374696f6e2068617320616c7265616479206265656e2063616c6c656400604482015290519081900360640190fd5b6009805460ff19166001179055600654600554600160a060020a0316600090815260076020526040902080549091019055565b600554600160a060020a031681565b60035481565b60065481565b60015481565b6002544210156104ec576040805160e560020a62461bcd02815260206004820152601560248201527f41756374696f6e206e6f742079657420656e6465640000000000000000000000604482015290519081900360640190fd5b60095460ff1615610547576040805160e560020a62461bcd02815260206004820152601f60248201527f61756374696f6e2068617320616c7265616479206265656e2063616c6c656400604482015290519081900360640190fd5b6009805460ff1916600117905560055460065460408051600160a060020a039093168352602083019190915280517fdaec4582d5d9595688c8c98545fdd1c696d41c6aeaeb636737e84ed2f5c00eda9281900390910190a160008054600654604051600160a060020a039092169281156108fc029290818181858888f193505050501580156105da573d6000803e3d6000fd5b505600a165627a7a72305820401bb6913644185e049f3fd8fb7f0d68b6145c0042da9c9023bc643a1d95b69f0029";

    public static final String FUNC_ENDED = "ended";

    public static final String FUNC_BID = "bid";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_AUCTIONENDTIME = "auctionEndTime";

    public static final String FUNC_GETPENDINGRETURNSBY = "getPendingReturnsBy";

    public static final String FUNC_DIGITALWORKID = "digitalWorkId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_CANCELAUCTION = "cancelAuction";

    public static final String FUNC_HIGHESTBIDDER = "highestBidder";

    public static final String FUNC_MINVALUE = "minValue";

    public static final String FUNC_HIGHESTBID = "highestBid";

    public static final String FUNC_AUCTIONSTARTTIME = "auctionStartTime";

    public static final String FUNC_ENDAUCTION = "endAuction";

    public static final Event HIGHESTBIDINCEREASED_EVENT = new Event("HighestBidIncereased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event AUCTIONENDED_EVENT = new Event("AuctionEnded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Auction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Auction(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Auction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Auction(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> ended() {
        final Function function = new Function(FUNC_ENDED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> bid(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> withdraw() {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> auctionEndTime() {
        final Function function = new Function(FUNC_AUCTIONENDTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getPendingReturnsBy(String _address) {
        final Function function = new Function(FUNC_GETPENDINGRETURNSBY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> digitalWorkId() {
        final Function function = new Function(FUNC_DIGITALWORKID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> cancelAuction() {
        final Function function = new Function(
                FUNC_CANCELAUCTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> highestBidder() {
        final Function function = new Function(FUNC_HIGHESTBIDDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> minValue() {
        final Function function = new Function(FUNC_MINVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> highestBid() {
        final Function function = new Function(FUNC_HIGHESTBID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> auctionStartTime() {
        final Function function = new Function(FUNC_AUCTIONSTARTTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> endAuction() {
        final Function function = new Function(
                FUNC_ENDAUCTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<HighestBidIncereasedEventResponse> getHighestBidIncereasedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(HIGHESTBIDINCEREASED_EVENT, transactionReceipt);
        ArrayList<HighestBidIncereasedEventResponse> responses = new ArrayList<HighestBidIncereasedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            HighestBidIncereasedEventResponse typedResponse = new HighestBidIncereasedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<HighestBidIncereasedEventResponse> highestBidIncereasedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, HighestBidIncereasedEventResponse>() {
            @Override
            public HighestBidIncereasedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(HIGHESTBIDINCEREASED_EVENT, log);
                HighestBidIncereasedEventResponse typedResponse = new HighestBidIncereasedEventResponse();
                typedResponse.log = log;
                typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<HighestBidIncereasedEventResponse> highestBidIncereasedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(HIGHESTBIDINCEREASED_EVENT));
        return highestBidIncereasedEventFlowable(filter);
    }

    public List<AuctionEndedEventResponse> getAuctionEndedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUCTIONENDED_EVENT, transactionReceipt);
        ArrayList<AuctionEndedEventResponse> responses = new ArrayList<AuctionEndedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuctionEndedEventResponse typedResponse = new AuctionEndedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AuctionEndedEventResponse> auctionEndedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AuctionEndedEventResponse>() {
            @Override
            public AuctionEndedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(AUCTIONENDED_EVENT, log);
                AuctionEndedEventResponse typedResponse = new AuctionEndedEventResponse();
                typedResponse.log = log;
                typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AuctionEndedEventResponse> auctionEndedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(AUCTIONENDED_EVENT));
        return auctionEndedEventFlowable(filter);
    }

    @Deprecated
    public static Auction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Auction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Auction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Auction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Auction load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Auction(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Auction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Auction(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Auction> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _owner, BigInteger workId, BigInteger minimum, BigInteger startTime, BigInteger endTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(workId), 
                new org.web3j.abi.datatypes.generated.Uint256(minimum), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime)));
        return deployRemoteCall(Auction.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Auction> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _owner, BigInteger workId, BigInteger minimum, BigInteger startTime, BigInteger endTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(workId), 
                new org.web3j.abi.datatypes.generated.Uint256(minimum), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime)));
        return deployRemoteCall(Auction.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Auction> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _owner, BigInteger workId, BigInteger minimum, BigInteger startTime, BigInteger endTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(workId), 
                new org.web3j.abi.datatypes.generated.Uint256(minimum), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime)));
        return deployRemoteCall(Auction.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Auction> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _owner, BigInteger workId, BigInteger minimum, BigInteger startTime, BigInteger endTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(workId), 
                new org.web3j.abi.datatypes.generated.Uint256(minimum), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime)));
        return deployRemoteCall(Auction.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class HighestBidIncereasedEventResponse {
        public Log log;

        public String bidder;

        public BigInteger amount;
    }

    public static class AuctionEndedEventResponse {
        public Log log;

        public String winner;

        public BigInteger amount;
    }
}
