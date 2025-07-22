package core.tools

import java.io.BufferedWriter
import java.io.FileWriter
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec

/**
 * Utility functions for RSA key pair creation, generates
 * a simple 1024-bit RSA key pair.
 * @author Nikki
 */
object RSAKeyGen {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val factory = KeyFactory.getInstance("RSA")
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(1024)
            val keypair = keyGen.genKeyPair()
            val privateKey = keypair.private
            val publicKey = keypair.public

            val privateSpec = factory.getKeySpec(privateKey, RSAPrivateKeySpec::class.java)
            writeKey("rsapriv", privateSpec.modulus, privateSpec.privateExponent)

            val publicSpec = factory.getKeySpec(publicKey, RSAPublicKeySpec::class.java)
            writeKey("rsapub", publicSpec.modulus, publicSpec.publicExponent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeKey(file: String?, modulus: BigInteger, exponent: BigInteger, ) {
        try {
            val writer = BufferedWriter(FileWriter(file))
            writer.write("private static final BigInteger RSA_MODULUS = new BigInteger(\"$modulus\");")
            writer.newLine()
            writer.newLine()
            writer.write("private static final BigInteger RSA_EXPONENT = new BigInteger(\"$exponent\");")
            writer.newLine()
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
