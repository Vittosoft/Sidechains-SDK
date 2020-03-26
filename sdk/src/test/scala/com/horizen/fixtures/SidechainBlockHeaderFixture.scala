package com.horizen.fixtures

import com.horizen.block.{SidechainBlock, SidechainBlockHeader}
import com.horizen.utils.{MerklePath, Utils}

import scala.util.Random
import scorex.util.bytesToId
import java.util.{ArrayList => JArrayList}

import com.horizen.proof.Signature25519
import com.horizen.vrf.VRFKeyGenerator

trait SidechainBlockHeaderFixture extends BoxFixture {

  def createUnsignedBlockHeader(seed: Long = 123134L): (SidechainBlockHeader, ForgerBoxGenerationMetadata) = {
    val random: Random = new Random(seed)

    val parentId = new Array[Byte](32)
    random.nextBytes(parentId)

    val (forgerBox, forgerMetadata) = ForgerBoxFixture.generateForgerBox(seed)
    val vrfProof = VRFKeyGenerator.generate(Array.fill(32)(seed.toByte))._1.prove(Array.fill(32)((seed + 1).toByte))

    val merklePath = new MerklePath(new JArrayList())

    val transactionsRootHash = Utils.ZEROS_HASH

    val mainchainRootHash = Utils.ZEROS_HASH

    val ommersRootHash = Utils.ZEROS_HASH


    val unsignedHeader = SidechainBlockHeader(
      SidechainBlock.BLOCK_VERSION,
      bytesToId(parentId),
      random.nextInt(100) + 10000,
      forgerBox,
      merklePath,
      vrfProof,
      transactionsRootHash,
      mainchainRootHash,
      ommersRootHash,
      0,
      new Signature25519(new Array[Byte](Signature25519.SIGNATURE_LENGTH))
    )

    (unsignedHeader, forgerMetadata)
  }
}