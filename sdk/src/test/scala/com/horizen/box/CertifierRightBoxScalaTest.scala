package com.horizen.box

import com.fasterxml.jackson.databind.JsonNode
import com.horizen.proposition.PublicKey25519Proposition
import com.horizen.serialization.ApplicationJsonSerializer
import com.horizen.utils.{BytesUtils, Ed25519}
import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import scorex.core.utils.ScorexEncoder

class CertifierRightBoxScalaTest
  extends JUnitSuite
{

  @Test
  def testToJson(): Unit = {
    val seed = "12345".getBytes
    val keyPair = Ed25519.createKeyPair(seed)
    val privateKey = keyPair.getKey
    val publicKey = keyPair.getValue

    val proposition = new PublicKey25519Proposition(publicKey)
    val nonce = 12345
    val value = 10
    val minimumWithdrawalEpoch = 5
    val box = new CertifierRightBox(proposition, nonce, value, minimumWithdrawalEpoch)

    val serializer = ApplicationJsonSerializer.getInstance()
    serializer.setDefaultConfiguration()

    val jsonStr = serializer.serialize(box)

    val node: JsonNode = serializer.getObjectMapper().readTree(jsonStr)

    assertEquals("Json must contain only 1 proposition.",
      1, node.findValues("proposition").size())
    assertEquals("Proposition json content must be the same.",
      BytesUtils.toHexString(proposition.pubKeyBytes()),
      node.path("proposition").path("publicKey").asText())

    assertEquals("Json must contain only 1 id.",
      1, node.findValues("id").size())
    assertTrue("Id json value must be the same.",
      box.id().sameElements(ScorexEncoder.default.decode(node.path("id").asText()).get))

    assertEquals("Json must contain only 1 nonce.",
      1, node.findValues("nonce").size())
    assertEquals("Nonce json value must be the same.",
      box.nonce(), node.path("nonce").asLong())

    assertEquals("Json must contain only 1 value.",
      1, node.findValues("value").size())
    assertEquals("Value json value must be the same.",
      box.value(), node.path("value").asLong())

    assertEquals("Json must contain only 1 activeFromWithdrawalEpoch.",
      1, node.findValues("activeFromWithdrawalEpoch").size())
    assertEquals("ActiveFromWithdrawalEpoch json value must be the same.",
      box.activeFromWithdrawalEpoch(), node.path("activeFromWithdrawalEpoch").asLong())
  }
}

