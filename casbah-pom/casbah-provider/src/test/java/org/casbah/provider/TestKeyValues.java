/*******************************************************************************
 * Copyright (C) 2010 Marco Sandrini
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.casbah.provider;

import java.math.BigInteger;

public abstract class TestKeyValues {

	public static final BigInteger MODULUS = new BigInteger(
			"00b394b1f8f3d2e37ca36d51249e0a" + "b907f36d622fae11834e76d8d62a80"
					+ "29b30449011c241bec94ef2327e4fc"
					+ "4592f0424c5844f03191913f676a26"
					+ "836efcc57a2de0f0537b514bd2b462"
					+ "234c8dc54f45e181cce6d18d09687d"
					+ "2c06c2cb64e3c9739cbc90d561f97f"
					+ "a27f8289910fdf8babb403227b3553"
					+ "3e16ebfdcc1ea2fa41fcc9c94c96e5"
					+ "15349d6d6d686f287635d69c806d02"
					+ "e6eb677c72226f504140fc1a9f4764"
					+ "f0bcef18132660b15da2ab7a5114c2"
					+ "b750cf15bbd49db0f08b5608ab8724"
					+ "0e616f6dd93a8079c841be4809847c"
					+ "167ffacf6897d64582d24e60de12dd"
					+ "f77ef7e74b5c7bd064c3880d3d3d50"
					+ "793cd0d4325b0f84119b85fcc9e8c2" + "e91d", 16);

	public static final BigInteger PUBLIC_EXPONENT = new BigInteger("65537");

	public static final BigInteger PRIVATE_EXPONENT = new BigInteger(
			"61654c01117c5714ca2e07cc2c4af4" + "7f736ee7c9a446ab486afcbf6add87"
					+ "c0279ad0974a9e3a79ac190d120c20"
					+ "4fc92eabe953ba51a7f976c1c7b88b"
					+ "a02e478c3445c31203d7db9fb36566"
					+ "6fa69454a239e404154ba18a70b932"
					+ "9f981667e982e0c9291c041ed70a7a"
					+ "3f5db4ee900555e312068135017edc"
					+ "35e2580bd4b1dfb7f153d4189d5131"
					+ "81c01d3dca5f1766c5be16ccf79079"
					+ "dcad8c1198d317048007fdd59275fd"
					+ "c0da3ca708e0a0c31d4eb8634fd0b0"
					+ "d1a1f5950c1939208edd02ad12ff1c"
					+ "933851ad2b50f9362fc788f61d32d4"
					+ "4d40a5ec9463fdb3eade2ca8241233"
					+ "041720075e013216e4700667e7502c"
					+ "743a3a5a17433cdae69107590b42c0" + "01", 16);

	public static final BigInteger PRIME1 = new BigInteger(
			"00e202c102149916c8f7af19a91ca8" + "d41a9a6a839c763a2630ce1440c43d"
					+ "9b48998d3e2b4af25b007e3e91b6b5"
					+ "68bcbd60eed7366450a1a61f5c64bd"
					+ "f499454c4aff84af95a47367c105a7"
					+ "17b990e46021cc7959c627ef4380f9"
					+ "078c72b7f5c9f06d9570e1ddd6b5ac"
					+ "639b92df8d28d5acadf69851457f7a" + "edf4a8b1955a828b1d",
			16);

	public static final BigInteger PRIME2 = new BigInteger(
			"00cb68c965a320628a6777029f7b47" + "a4a984b45b5235c68be320eb3e1cd0"
					+ "3785a23c800c84d137c1549f035a1c"
					+ "e3391e48f651af951298708a4ebfda"
					+ "c57ef5ee9b079046fb94c80b82d3cf"
					+ "b30f5e03a0d0451fa7b269ba04c1f3"
					+ "dd55f9e9bd958b3f6c6bf7e5f5b91e"
					+ "3df456d25cd65dbb69dee947ce89b9" + "c10aa1bb24bfd57601",
			16);

	public static final BigInteger EXPONENT1 = new BigInteger(
			"521561f7d9774839310b9d2c384670" + "d240a6d2a9ee9e6be469d41d39a88e"
					+ "4f2dc58880bcb58cab7f2e4649af55"
					+ "1cc742ff3b967b9475411a3eba2feb"
					+ "38075d1018011eac208ff16cbf1ad2"
					+ "9908b088e17d9bcd2914b3e7365e3b"
					+ "899cd88722127a1a3e6729e4a312bb"
					+ "1273a35167354383e2fdaaf6acb7fe" + "d84eb4406420e959", 16);

	public static final BigInteger EXPONENT2 = new BigInteger(
			"00c8e466f400963d7c600f4401eaa9" + "a950db059da3a604bfa7c49d512cf7"
					+ "dfbe15ab16f44640b38637630cd674"
					+ "ed5d3e31e5446bdb19108125b7fcf7"
					+ "be5253e6222ae82d92cf307b5dec7f"
					+ "97633800f15480d070b95e7e1fb0c6"
					+ "7ef528ebd717d8fcfcc1ed85aa97f7"
					+ "ec4c636b43d15ba57b3c72bd3e98fb" + "625273c4f6d897a401",
			16);

	public static final BigInteger COEFFICIENT = new BigInteger(
			"6574ffcedc39de2b92ae6239cf8b8b" + "e8c10460091163545741a2fdd1033d"
					+ "2abc26d8bef4fc7b7c0f0e439ac987"
					+ "1ef0a15b47f6a10f24e8cbcf5803b2"
					+ "ee7b2fa4e45ec8a04e59f1137b2c89"
					+ "6292e878e4dd585cbcd23e3c31b386"
					+ "c4787fcdea12822b01c23b76dfbfe0"
					+ "01ab581056a557d7d92dac65c1136c" + "1dc07c3b9bfee89b", 16);

}
