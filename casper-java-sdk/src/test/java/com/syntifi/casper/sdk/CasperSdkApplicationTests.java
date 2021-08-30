package com.syntifi.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import com.syntifi.casper.sdk.filter.block.CasperBlockByHashFilter;
import com.syntifi.casper.sdk.filter.block.CasperBlockByHeightFilter;
import com.syntifi.casper.sdk.service.CasperService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link CasperService}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
class CasperSdkApplicationTests {
	private static Logger LOGGER = LoggerFactory.getLogger(CasperSdkApplicationTests.class);

	private static String testIP = "157.230.101.249";
	private static int testPort = 7777;

	private static CasperService casperService;

	@BeforeAll
	public static void setUp() throws MalformedURLException {
		casperService = CasperService.usingPeer(testIP, testPort);
	}

	/**
	 * Test if get block matches requested by height
	 */
	@Test
	void testIfBlockReturnedMatchesRequestedByHeight() {
		var blocks_to_check = 3;
		for (var i = 0; i < blocks_to_check; i++) {
			var result = casperService.getBlock(new CasperBlockByHeightFilter(i));
			assertEquals(result.getBlock().getHeader().getHeight(), i);
		}
	}

	/**
	 * Test if get block matches requested by hash
	 */
	@Test
	void testIfBlockReturnedMatchesRequestedByHash() {
		var blocks_to_check = 3;		
		for (var i = 0; i < blocks_to_check; i++) {
			LOGGER.debug(String.format("Testing with block height %d", i));
			var resultByHeight = casperService.getBlock(new CasperBlockByHeightFilter(i));
			var hash = resultByHeight.getBlock().getHash();
			var resultByHash = casperService.getBlock(new CasperBlockByHashFilter(hash));

			assertEquals(resultByHash.getBlock().getHash(), hash);
		}
	}

	/**
	 * Retrieve peers list and assert it has elements
	 * 
	 * @throws Throwable
	 */
	@Test
	void retrieveNonEmptyListOfPeers() {
		var peerData = casperService.getPeerData();

		assertNotNull(peerData);
		assertFalse(peerData.getPeers().isEmpty());
	}

	@Test
	void retrieveLastBlock() {
		var blockData = casperService.getBlock();

		assertNotNull(blockData);
	}

	// "7d9462f91e33e3df16b1eadd5d3767af942470cc3422971377594cf82e7e4fc4"
	// "d8f921f792064202749cbd286379acb6c0fdb3d3d0526c2c7e92c03ff2c26c1d"
	@Test
	void getBlockByHash() {
		var blockData = casperService.getBlock(
				new CasperBlockByHashFilter("d8f921f792064202749cbd286379acb6c0fdb3d3d0526c2c7e92c03ff2c26c1d"));

		assertNotNull(blockData);
	}

	@Test
	void getBlockByHeight() {
		var blockData = casperService.getBlock(new CasperBlockByHeightFilter(5));

		assertNotNull(blockData);
	}

	@Test
	void getBlockState() {
		var stateRootHash = "c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff";
		var key = "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0";
		List<String> path = Arrays.asList("special_value");
		var result = casperService.getStateItem(stateRootHash, key, path);

		assertNotNull(result);
	}
}
