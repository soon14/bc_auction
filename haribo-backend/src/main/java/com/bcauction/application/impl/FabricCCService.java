package com.bcauction.application.impl;

import com.bcauction.application.IFabricCCService;
import com.bcauction.domain.CommonUtil;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.FabricUser;
import com.google.protobuf.ByteString;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.helper.Utils;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.InitBinder;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.http.HttpService;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FabricCCService implements IFabricCCService
{
	private static final Logger logger = LoggerFactory.getLogger(FabricCCService.class);

	private HFClient hfClient;
	private Channel channel;
	private HFCAClient hfcaClient=null;
	/**
	 * 패브릭 네트워크를 이용하기 위한 정보
	 */
	@Value("${fabric.ca-server.url}")
	private String CA_SERVER_URL;
	@Value("${fabric.ca-server.admin.name}")
	private String CA_SERVER_ADMIN_NAME;
	@Value("${fabric.ca-server.pem.file}")
	private String CA_SERVER_PEM_FILE;
	@Value("${fabric.org.name}")
	private String ORG_NAME;
	@Value("${fabric.org.msp.name}")
	private String ORG_MSP_NAME;
	@Value("${fabric.org.admin.name}")
	private String ORG_ADMIN_NAME;
	@Value("${fabric.peer.name}")
	private String PEER_NAME;
	@Value("${fabric.peer.url}")
	private String PEER_URL;
	@Value("${fabric.peer.pem.file}")
	private String PEER_PEM_FILE;
	@Value("${fabric.orderer.name}")
	private String ORDERER_NAME;
	@Value("${fabric.orderer.url}")
	private String ORDERER_URL;
	@Value("${fabric.orderer.pem.file}")
	private String ORDERER_PEM_FILE;
	@Value("${fabric.org.user.name}")
	private String USER_NAME;
	@Value("${fabric.org.user.secret}")
	private String USER_SECRET;
	@Value("${fabric.channel.name}")
	private String CHANNEL_NAME;


	/**
	 * 체인코드를 이용하기 위하여
	 * 구축해놓은 패브릭 네트워크의 채널을 가져오는
	 * 기능을 구현한다.
	 * 여기에서 this.channel의 값을 초기화 한다

	 */
	private void loadChannel(){
		try {

			// create HFCAClient instance
			Properties hfca_properties=getPropertiesWith(CA_SERVER_PEM_FILE);
			hfcaClient=HFCAClient.createNewInstance(CA_SERVER_ADMIN_NAME, CA_SERVER_URL, hfca_properties);
			
//			// use cryptoSuite
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			hfcaClient.setCryptoSuite(cryptoSuite);
			
			//set admin_userContext
			FabricUser admin_userContext=new FabricUser();
			admin_userContext.setName(ORG_ADMIN_NAME);
			admin_userContext.setAfflication(ORG_NAME);
			admin_userContext.setMspid(ORG_MSP_NAME);
			
			//set enrollment
			Enrollment enrollment=hfcaClient.enroll(admin_userContext.getName(), USER_SECRET);
			admin_userContext.setEnrollment(enrollment);
			
			//register hfclient
			hfClient=hfClient.createNewInstance();
			hfClient.setCryptoSuite(cryptoSuite);
			hfClient.setUserContext(admin_userContext);
			
			//pem 읽기(peer)
			Properties peer_properties=getPropertiesWith(PEER_PEM_FILE);
			Peer peer=hfClient.newPeer(PEER_NAME, PEER_URL, peer_properties);
			
			//pem 읽기(orderer)
			Properties orderer_properties=getPropertiesWith(ORDERER_PEM_FILE);
			Orderer orderer=hfClient.newOrderer(ORDERER_NAME, ORDERER_URL, orderer_properties);
			
			channel=hfClient.newChannel(CHANNEL_NAME);
			channel.addPeer(peer);
			channel.addOrderer(orderer);
			channel.initialize();
			System.out.println("채널 로드 완료");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private Properties getPropertiesWith(String filename) {
		Properties properties = new Properties();
		properties.put("pemBytes", CommonUtil.readString(filename).getBytes());
		properties.setProperty("sslProvider", "openSSL");
		properties.setProperty("negotiationType", "TLS");
		return properties;
	}

	/**
	 * 소유권 등록을 위해 체인코드 함수를 차례대로 호출한다.
	 * @param 소유자
	 * @param 작품id
	 * @return FabricAsset
	 */
	@Override
	public FabricAsset registerOwnership(final long 소유자, final long 작품id){
		if(this.channel == null)
			loadChannel();

		boolean res = registerAsset(작품id, 소유자);
		
		logger.info("stop");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("start");
		System.out.println("res : "+res);
		
		if(!res)
			return null;
		
		res = confirmTimestamp(작품id);
		
		if(!res)
			return null;

		return query(작품id);
	}

	/**
	 * 소유권 이전을 위해 체인코드 함수를 차례대로 호출한다.
	 * @param from
	 * @param to
	 * @param 작품id
	 * @return List<FabricAsset
	 */
	@Override
	public List<FabricAsset> transferOwnership(final long from, final long to, final long 작품id) {
		if(this.channel == null)
			loadChannel();

		List<FabricAsset> assets = new ArrayList<>();
		boolean res = this.expireAssetOwnership(작품id, from);
		if(!res) return null;
		FabricAsset expired = query(작품id);
		if(expired == null) return null;
		assets.add(expired);

		res = this.updateAssetOwnership(작품id, to);
		if(!res) return null;
		FabricAsset transferred = query(작품id);
		if(transferred == null) return null;
		assets.add(transferred);

		return assets;
	}

	/**
	 * 소유권 소멸을 위해 체인코드 함수를 호출한다.
	 * @param 작품id
	 * @param 소유자id
	 * @return FabricAsset
	 */
	@Override
	public FabricAsset expireOwnership(final long 작품id, final long 소유자id) {
		if(this.channel == null)
			loadChannel();

		boolean res = this.expireAssetOwnership(작품id, 소유자id);
		if(!res) return null;

		return query(작품id);
	}

	/**
	 * 체인코드 registerAsset 함수를 호출하는 메소드
	 * @param 작품id
	 * @param 소유자
	 * @return boolean
	 */
	private boolean registerAsset(final long 작품id, final long 소유자) {
		// TODO
		String[] args=new String[2];
		args[0]=Long.toString(작품id);
		args[1]=Long.toString(소유자);
		
		TransactionProposalRequest tpr=hfClient.newTransactionProposalRequest();
		ChaincodeID faChaincodeID=ChaincodeID.newBuilder().setName("asset").build();
		
		tpr.setChaincodeID(faChaincodeID);
		tpr.setFcn("registerAsset");
		tpr.setArgs(args);
		
		try {
			Collection<ProposalResponse> responses=channel.sendTransactionProposal(tpr);
			for (ProposalResponse res: responses) {
				Status status=res.getStatus();
				logger.info(status.toString());
			}
			// block 생성됨
			channel.sendTransaction(responses);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		
	}

	/**
	 * 체인코드 confirmTimestamp 함수를 호출하는 메소드
	 * @param 작품id
	 * @return
	 */
	private boolean confirmTimestamp(final long 작품id){
		// TODO
		String[] args=new String[1];
		args[0]=Long.toString(작품id);
		
		TransactionProposalRequest tpr=hfClient.newTransactionProposalRequest();
		ChaincodeID faChaincodeID=ChaincodeID.newBuilder().setName("asset").build();
		
		tpr.setChaincodeID(faChaincodeID);
		tpr.setFcn("confirmTimestamp");
		tpr.setArgs(args);
		
		try {
			Collection<ProposalResponse> responses=channel.sendTransactionProposal(tpr);
			for (ProposalResponse res: responses) {
				Status status=res.getStatus();
				logger.info(status.toString());
			}
			channel.sendTransaction(responses);
			return true;
			// block 생성됨
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}

	/**
	 * 체인코드 expireAssetOwnership를 호출하는 메소드
	 * @param 작품id
	 * @param 소유자
	 * @return
	 */
	private boolean expireAssetOwnership(final long 작품id, final long 소유자) {
		// TODO
		return false;
	}

	/**
	 * 체인코드 updateAssetOwnership를 호출하는 메소드
	 * @param 작품id
	 * @param to
	 * @return
	 */
	private boolean updateAssetOwnership(final long 작품id, final long to) {
		// TODO
		return false;
	}

	/**
	 * 체인코드 queryHistory 함수를 호출하는 메소드
	 * @param 작품id
	 * @return
	 */
	@Override
	public List<FabricAsset> queryHistory(final long 작품id){
		if(this.hfClient == null || this.channel == null)
			loadChannel();

		return null;
	}

	/**
	 * 체인코드 query 함수를 호출하는 메소드
	 * @param 작품id
	 * @return
	 */
	@Override
	public FabricAsset query(final long 작품id){
		if(this.hfClient == null || this.channel == null)
			loadChannel();

		FabricAsset tmp=null;
		String response=null;
		
		String []args=new String[1];
		args[0]=Long.toString(작품id);
		
		QueryByChaincodeRequest qbr=hfClient.newQueryProposalRequest();
		ChaincodeID faChaincodeID=ChaincodeID.newBuilder().setName("asset").build();
		
		qbr.setChaincodeID(faChaincodeID);
		qbr.setFcn("query");
		qbr.setArgs(args);
		
		Collection<ProposalResponse> responseQuery;
		try {
			responseQuery = channel.queryByChaincode(qbr);
			for (ProposalResponse res: responseQuery) {
				response=new String(res.getChaincodeActionResponsePayload());
				System.out.println("query호출");
				logger.info(res.getMessage());
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static FabricAsset getAssetRecord(final JsonObject rec)
	{
		FabricAsset asset = new FabricAsset();

		asset.setAssetId(rec.getString("assetID"));
		asset.setOwner(rec.getString("owner"));
		asset.setCreatedAt(rec.getString("createdAt"));
		asset.setExpiredAt(rec.getString("expiredAt"));

		logger.info("Work " + rec.getString("assetID") + " by Owner " + rec.getString("owner") + ": "+
				            rec.getString("createdAt") + " ~ " + rec.getString("expiredAt"));

		return asset;
	}
}
