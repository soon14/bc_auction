package com.bcauction.domain.wrapper;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
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

import com.bcauction.domain.wrapper.AuctionFactoryContract.AuctionCreatedEventResponse;
import com.bcauction.domain.wrapper.AuctionFactoryContract.NewAuctionEventResponse;
import com.bcauction.domain.wrapper.AuctionFactoryContract.OwnershipTransferredEventResponse;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */

/**
 * TODO
 * AuctionFactory.sol을
 * 솔리디티 컴파일러와 Web3 CLI를 이용하여
 * 자동생성된 솔리디티 스마트 컨트랙트의 Wrapper Class를
 * 해당 패키지에  AuctionFactoryContract.java로 추가한다.
 */
public class AuctionFactoryContract extends Contract {
	
	private static final String BINARY = "608060405234801561001057600080fd5b5060008054600160a060020a03191633179055610af3806100326000396000f3006080604052600436106100775763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631bbcd4a5811461007c5780633cec475a146100e1578063431f21da14610108578063571a26a0146101455780638da5cb5b1461015d578063f2fde38b14610172575b600080fd5b34801561008857600080fd5b50610091610195565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156100cd5781810151838201526020016100b5565b505050509050019250505060405180910390f35b3480156100ed57600080fd5b506100f66101f7565b60408051918252519081900360200190f35b34801561011457600080fd5b506101296004356024356044356064356101fd565b60408051600160a060020a039092168252519081900360200190f35b34801561015157600080fd5b50610129600435610369565b34801561016957600080fd5b50610129610391565b34801561017e57600080fd5b50610193600160a060020a03600435166103a0565b005b606060018054806020026020016040519081016040528092919081815260200182805480156101ed57602002820191906000526020600020905b8154600160a060020a031681526001909101906020018083116101cf575b5050505050905090565b60025481565b600080548190600160a060020a031686868686610218610434565b600160a060020a039095168552602085019390935260408085019290925260608401526080830191909152519081900360a001906000f080158015610261573d6000803e3d6000fd5b5060018054808201825560008281527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf6909101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0385811691821790925582546040805192835292166020820181905291810183905260806060820181815285549183018290529596507f0d314047f0d1e39dbbd521f0844c35332083a15926b906540faa68e1a4262b68958795939493929160a08301908490801561034f57602002820191906000526020600020905b8154600160a060020a03168152600190910190602001808311610331575b50509550505050505060405180910390a195945050505050565b600180548290811061037757fe5b600091825260209091200154600160a060020a0316905081565b600054600160a060020a031681565b600054600160a060020a031633146103b757600080fd5b600160a060020a03811615156103cc57600080fd5b60008054604051600160a060020a03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a36000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b60405161068380610445833901905600608060405234801561001057600080fd5b5060405160a0806106838339810160409081528151602083015191830151606084015160809094015160008054600160a060020a031916600160a060020a0390941693909317835560049390935560035560019290925560025561060990819061007a90396000f3006080604052600436106100c45763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166312fa6feb81146100c95780631998aeef146100f25780633ccfd60b146100fc5780634b449cba146101115780635b90dbc4146101385780636ba35e70146101595780638da5cb5b1461016e5780638fa8b7901461019f57806391f90157146101b4578063963e63c7146101c9578063d57bde79146101de578063eb54f9ec146101f3578063fe67a54b14610208575b600080fd5b3480156100d557600080fd5b506100de61021d565b604080519115158252519081900360200190f35b6100fa610226565b005b34801561010857600080fd5b506100de6102e9565b34801561011d57600080fd5b50610126610353565b60408051918252519081900360200190f35b34801561014457600080fd5b50610126600160a060020a0360043516610359565b34801561016557600080fd5b50610126610374565b34801561017a57600080fd5b5061018361037a565b60408051600160a060020a039092168252519081900360200190f35b3480156101ab57600080fd5b506100fa610389565b3480156101c057600080fd5b50610183610471565b3480156101d557600080fd5b50610126610480565b3480156101ea57600080fd5b50610126610486565b3480156101ff57600080fd5b5061012661048c565b34801561021457600080fd5b506100fa610492565b60095460ff1681565b600054600160a060020a031633141561023e57600080fd5b6006543411610297576040805160e560020a62461bcd02815260206004820152601e60248201527f546865726520616c7265616479206973206120686967686572206269642e0000604482015290519081900360640190fd5b600654156102c457600654600554600160a060020a03166000908152600760205260409020805490910190555b6005805473ffffffffffffffffffffffffffffffffffffffff19163317905534600655565b336000908152600760205260408120548181111561034a57336000818152600760205260408082208290555183156108fc0291849190818181858888f19350505050151561034a57336000908152600760205260408120829055915061034f565b600191505b5090565b60025481565b600160a060020a031660009081526007602052604090205490565b60045481565b600054600160a060020a031681565b6002544210156103e3576040805160e560020a62461bcd02815260206004820152601560248201527f41756374696f6e206e6f742079657420656e6465640000000000000000000000604482015290519081900360640190fd5b60095460ff161561043e576040805160e560020a62461bcd02815260206004820152601f60248201527f61756374696f6e2068617320616c7265616479206265656e2063616c6c656400604482015290519081900360640190fd5b6009805460ff19166001179055600654600554600160a060020a0316600090815260076020526040902080549091019055565b600554600160a060020a031681565b60035481565b60065481565b60015481565b6002544210156104ec576040805160e560020a62461bcd02815260206004820152601560248201527f41756374696f6e206e6f742079657420656e6465640000000000000000000000604482015290519081900360640190fd5b60095460ff1615610547576040805160e560020a62461bcd02815260206004820152601f60248201527f61756374696f6e2068617320616c7265616479206265656e2063616c6c656400604482015290519081900360640190fd5b6009805460ff1916600117905560055460065460408051600160a060020a039093168352602083019190915280517fdaec4582d5d9595688c8c98545fdd1c696d41c6aeaeb636737e84ed2f5c00eda9281900390910190a160008054600654604051600160a060020a039092169281156108fc029290818181858888f193505050501580156105da573d6000803e3d6000fd5b505600a165627a7a72305820401bb6913644185e049f3fd8fb7f0d68b6145c0042da9c9023bc643a1d95b69f0029a165627a7a72305820fed941fbaf6738bca61387a90443faccc6fca9c6bbea14b6bd76a6b863eb678c0029";

    public static final String FUNC_ALLAUCTIONS = "allAuctions";

    public static final String FUNC_CONNECTTEST = "connectTest";

    public static final String FUNC_CREATEAUCTION = "createAuction";

    public static final String FUNC_AUCTIONS = "auctions";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event AUCTIONCREATED_EVENT = new Event("AuctionCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Address>>() {}));
    ;

    public static final Event NEWAUCTION_EVENT = new Event("NewAuction", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected AuctionFactoryContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AuctionFactoryContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AuctionFactoryContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AuctionFactoryContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<List> allAuctions() {
        final Function function = new Function(FUNC_ALLAUCTIONS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<BigInteger> connectTest() {
        final Function function = new Function(FUNC_CONNECTTEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> createAuction(BigInteger workId, BigInteger minValue, BigInteger startTime, BigInteger endTime) {
        final Function function = new Function(
                FUNC_CREATEAUCTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(workId), 
                new org.web3j.abi.datatypes.generated.Uint256(minValue), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> auctions(BigInteger param0) {
        final Function function = new Function(FUNC_AUCTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<AuctionCreatedEventResponse> getAuctionCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUCTIONCREATED_EVENT, transactionReceipt);
        ArrayList<AuctionCreatedEventResponse> responses = new ArrayList<AuctionCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuctionCreatedEventResponse typedResponse = new AuctionCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.auctionContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.numAuctions = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.allAuctions = (List<String>) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AuctionCreatedEventResponse> auctionCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AuctionCreatedEventResponse>() {
            @Override
            public AuctionCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(AUCTIONCREATED_EVENT, log);
                AuctionCreatedEventResponse typedResponse = new AuctionCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.auctionContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.numAuctions = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.allAuctions = (List<String>) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AuctionCreatedEventResponse> auctionCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(AUCTIONCREATED_EVENT));
        return auctionCreatedEventFlowable(filter);
    }

    public List<NewAuctionEventResponse> getNewAuctionEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWAUCTION_EVENT, transactionReceipt);
        ArrayList<NewAuctionEventResponse> responses = new ArrayList<NewAuctionEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewAuctionEventResponse typedResponse = new NewAuctionEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.auctionContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.workId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.minValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.startTime = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.endTime = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewAuctionEventResponse> newAuctionEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewAuctionEventResponse>() {
            @Override
            public NewAuctionEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWAUCTION_EVENT, log);
                NewAuctionEventResponse typedResponse = new NewAuctionEventResponse();
                typedResponse.log = log;
                typedResponse.auctionContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.workId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.minValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.startTime = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.endTime = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewAuctionEventResponse> newAuctionEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWAUCTION_EVENT));
        return newAuctionEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    @Deprecated
    public static AuctionFactoryContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionFactoryContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AuctionFactoryContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionFactoryContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AuctionFactoryContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AuctionFactoryContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AuctionFactoryContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AuctionFactoryContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AuctionFactoryContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AuctionFactoryContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<AuctionFactoryContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AuctionFactoryContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AuctionFactoryContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AuctionFactoryContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AuctionFactoryContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AuctionFactoryContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class AuctionCreatedEventResponse {
        public Log log;

        public String auctionContract;

        public String owner;

        public BigInteger numAuctions;

        public List<String> allAuctions;
    }

    public static class NewAuctionEventResponse {
        public Log log;

        public String auctionContract;

        public String owner;

        public BigInteger workId;

        public BigInteger minValue;

        public BigInteger startTime;

        public BigInteger endTime;
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
	
}