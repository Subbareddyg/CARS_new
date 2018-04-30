package com.belk.car.product.integration.adapter;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * 
 * This class will act as the XML Registry component for configuring all dynamic elements that could be coming 
 * in the response for the new integration for cars with the PIM application. 
 * Currently this list needn't be maintained as the application will handle elements not defined as part
 * of this class we well. 
 *
 */
@XmlRegistry
public class ObjectFactory {
 
	public static final String SEC_SPEC_1601_ACCESSORIES = "sec_spec_1601-Accessories";
	@XmlElementDecl(name = SEC_SPEC_1601_ACCESSORIES)
	public JAXBElement<String> createSEC_SPEC_1601_ACCESSORIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1601_ACCESSORIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1602_BABY_KEEPSAKES_AND_GIFTS = "sec_spec_1602-Baby_Keepsakes_and_Gifts";
	@XmlElementDecl(name = SEC_SPEC_1602_BABY_KEEPSAKES_AND_GIFTS)
	public JAXBElement<String> createSEC_SPEC_1602_BABY_KEEPSAKES_AND_GIFTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1602_BABY_KEEPSAKES_AND_GIFTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1603_BOYS = "sec_spec_1603-Boys";
	@XmlElementDecl(name = SEC_SPEC_1603_BOYS)
	public JAXBElement<String> createSEC_SPEC_1603_BOYS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1603_BOYS), String.class, attribute);
	}

	public static final String SEC_SPEC_1607_HATS = "sec_spec_1607-Hats";
	@XmlElementDecl(name = SEC_SPEC_1607_HATS)
	public JAXBElement<String> createSEC_SPEC_1607_HATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1607_HATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1609_SOCKS = "sec_spec_1609-Socks";
	@XmlElementDecl(name = SEC_SPEC_1609_SOCKS)
	public JAXBElement<String> createSEC_SPEC_1609_SOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1609_SOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1610_TIES = "sec_spec_1610-Ties";
	@XmlElementDecl(name = SEC_SPEC_1610_TIES)
	public JAXBElement<String> createSEC_SPEC_1610_TIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1610_TIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1611_GIRLS = "sec_spec_1611-Girls";
	@XmlElementDecl(name = SEC_SPEC_1611_GIRLS)
	public JAXBElement<String> createSEC_SPEC_1611_GIRLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1611_GIRLS), String.class, attribute);
	}

	public static final String SEC_SPEC_1614_HAIR_ACCESSORIES = "sec_spec_1614-Hair_Accessories";
	@XmlElementDecl(name = SEC_SPEC_1614_HAIR_ACCESSORIES)
	public JAXBElement<String> createSEC_SPEC_1614_HAIR_ACCESSORIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1614_HAIR_ACCESSORIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1615_HATS = "sec_spec_1615-Hats";
	@XmlElementDecl(name = SEC_SPEC_1615_HATS)
	public JAXBElement<String> createSEC_SPEC_1615_HATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1615_HATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1616_PURSES_AND_BACKPACKS = "sec_spec_1616-Purses_and_Backpacks";
	@XmlElementDecl(name = SEC_SPEC_1616_PURSES_AND_BACKPACKS)
	public JAXBElement<String> createSEC_SPEC_1616_PURSES_AND_BACKPACKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1616_PURSES_AND_BACKPACKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1618_SOCKS = "sec_spec_1618-Socks";
	@XmlElementDecl(name = SEC_SPEC_1618_SOCKS)
	public JAXBElement<String> createSEC_SPEC_1618_SOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1618_SOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1619_TIGHTS = "sec_spec_1619-Tights";
	@XmlElementDecl(name = SEC_SPEC_1619_TIGHTS)
	public JAXBElement<String> createSEC_SPEC_1619_TIGHTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1619_TIGHTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1620_GLASSES = "sec_spec_1620-Glasses";
	@XmlElementDecl(name = SEC_SPEC_1620_GLASSES)
	public JAXBElement<String> createSEC_SPEC_1620_GLASSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1620_GLASSES), String.class, attribute);
	}

	public static final String SEC_SPEC_1621_READING_GLASSES = "sec_spec_1621-Reading_Glasses";
	@XmlElementDecl(name = SEC_SPEC_1621_READING_GLASSES)
	public JAXBElement<String> createSEC_SPEC_1621_READING_GLASSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1621_READING_GLASSES), String.class, attribute);
	}

	public static final String SEC_SPEC_1622_SUNGLASSES = "sec_spec_1622-Sunglasses";
	@XmlElementDecl(name = SEC_SPEC_1622_SUNGLASSES)
	public JAXBElement<String> createSEC_SPEC_1622_SUNGLASSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1622_SUNGLASSES), String.class, attribute);
	}

	public static final String SEC_SPEC_1623_JEWELRY = "sec_spec_1623-Jewelry";
	@XmlElementDecl(name = SEC_SPEC_1623_JEWELRY)
	public JAXBElement<String> createSEC_SPEC_1623_JEWELRY(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1623_JEWELRY), String.class, attribute);
	}

	public static final String SEC_SPEC_1625_BEADS_AND_CHARMS = "sec_spec_1625-Beads_and_Charms";
	@XmlElementDecl(name = SEC_SPEC_1625_BEADS_AND_CHARMS)
	public JAXBElement<String> createSEC_SPEC_1625_BEADS_AND_CHARMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1625_BEADS_AND_CHARMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1626_BRACELETS = "sec_spec_1626-Bracelets";
	@XmlElementDecl(name = SEC_SPEC_1626_BRACELETS)
	public JAXBElement<String> createSEC_SPEC_1626_BRACELETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1626_BRACELETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1628_EARRINGS = "sec_spec_1628-Earrings";
	@XmlElementDecl(name = SEC_SPEC_1628_EARRINGS)
	public JAXBElement<String> createSEC_SPEC_1628_EARRINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1628_EARRINGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1629_JEWELRY_BOXES_AND_STORAGE = "sec_spec_1629-Jewelry_Boxes_and_Storage";
	@XmlElementDecl(name = SEC_SPEC_1629_JEWELRY_BOXES_AND_STORAGE)
	public JAXBElement<String> createSEC_SPEC_1629_JEWELRY_BOXES_AND_STORAGE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1629_JEWELRY_BOXES_AND_STORAGE), String.class, attribute);
	}

	public static final String SEC_SPEC_1631_NECKLACES = "sec_spec_1631-Necklaces";
	@XmlElementDecl(name = SEC_SPEC_1631_NECKLACES)
	public JAXBElement<String> createSEC_SPEC_1631_NECKLACES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1631_NECKLACES), String.class, attribute);
	}

	public static final String SEC_SPEC_1633_RINGS = "sec_spec_1633-Rings";
	@XmlElementDecl(name = SEC_SPEC_1633_RINGS)
	public JAXBElement<String> createSEC_SPEC_1633_RINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1633_RINGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1634_SETS = "sec_spec_1634-Sets";
	@XmlElementDecl(name = SEC_SPEC_1634_SETS)
	public JAXBElement<String> createSEC_SPEC_1634_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1634_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1635_WATCHES = "sec_spec_1635-Watches";
	@XmlElementDecl(name = SEC_SPEC_1635_WATCHES)
	public JAXBElement<String> createSEC_SPEC_1635_WATCHES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1635_WATCHES), String.class, attribute);
	}

	public static final String SEC_SPEC_1636_LUGGAGE = "sec_spec_1636-Luggage";
	@XmlElementDecl(name = SEC_SPEC_1636_LUGGAGE)
	public JAXBElement<String> createSEC_SPEC_1636_LUGGAGE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1636_LUGGAGE), String.class, attribute);
	}

	public static final String SEC_SPEC_1641_LUGGAGE_SETS = "sec_spec_1641-Luggage_Sets";
	@XmlElementDecl(name = SEC_SPEC_1641_LUGGAGE_SETS)
	public JAXBElement<String> createSEC_SPEC_1641_LUGGAGE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1641_LUGGAGE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1648_MEN = "sec_spec_1648-Men";
	@XmlElementDecl(name = SEC_SPEC_1648_MEN)
	public JAXBElement<String> createSEC_SPEC_1648_MEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1648_MEN), String.class, attribute);
	}

	public static final String SEC_SPEC_1649_BAGS = "sec_spec_1649-Bags";
	@XmlElementDecl(name = SEC_SPEC_1649_BAGS)
	public JAXBElement<String> createSEC_SPEC_1649_BAGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1649_BAGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1653_BELTS_AND_SUSPENDERS = "sec_spec_1653-Belts_and_Suspenders";
	@XmlElementDecl(name = SEC_SPEC_1653_BELTS_AND_SUSPENDERS)
	public JAXBElement<String> createSEC_SPEC_1653_BELTS_AND_SUSPENDERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1653_BELTS_AND_SUSPENDERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1657_HATS = "sec_spec_1657-Hats";
	@XmlElementDecl(name = SEC_SPEC_1657_HATS)
	public JAXBElement<String> createSEC_SPEC_1657_HATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1657_HATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1658_SCARVES = "sec_spec_1658-Scarves";
	@XmlElementDecl(name = SEC_SPEC_1658_SCARVES)
	public JAXBElement<String> createSEC_SPEC_1658_SCARVES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1658_SCARVES), String.class, attribute);
	}

	public static final String SEC_SPEC_1659_SOCKS = "sec_spec_1659-Socks";
	@XmlElementDecl(name = SEC_SPEC_1659_SOCKS)
	public JAXBElement<String> createSEC_SPEC_1659_SOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1659_SOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1660_TIES_AND_POCKET_SQUARES = "sec_spec_1660-Ties_and_Pocket_Squares";
	@XmlElementDecl(name = SEC_SPEC_1660_TIES_AND_POCKET_SQUARES)
	public JAXBElement<String> createSEC_SPEC_1660_TIES_AND_POCKET_SQUARES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1660_TIES_AND_POCKET_SQUARES), String.class, attribute);
	}

	public static final String SEC_SPEC_1664_BOW_TIES = "sec_spec_1664-Bow_Ties";
	@XmlElementDecl(name = SEC_SPEC_1664_BOW_TIES)
	public JAXBElement<String> createSEC_SPEC_1664_BOW_TIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1664_BOW_TIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1665_NECKTIES = "sec_spec_1665-Neckties";
	@XmlElementDecl(name = SEC_SPEC_1665_NECKTIES)
	public JAXBElement<String> createSEC_SPEC_1665_NECKTIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1665_NECKTIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1672_UMBRELLAS = "sec_spec_1672-Umbrellas";
	@XmlElementDecl(name = SEC_SPEC_1672_UMBRELLAS)
	public JAXBElement<String> createSEC_SPEC_1672_UMBRELLAS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1672_UMBRELLAS), String.class, attribute);
	}

	public static final String SEC_SPEC_1673_WALLETS = "sec_spec_1673-Wallets";
	@XmlElementDecl(name = SEC_SPEC_1673_WALLETS)
	public JAXBElement<String> createSEC_SPEC_1673_WALLETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1673_WALLETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1680_BELTS = "sec_spec_1680-Belts";
	@XmlElementDecl(name = SEC_SPEC_1680_BELTS)
	public JAXBElement<String> createSEC_SPEC_1680_BELTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1680_BELTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1683_HANDBAGS = "sec_spec_1683-Handbags";
	@XmlElementDecl(name = SEC_SPEC_1683_HANDBAGS)
	public JAXBElement<String> createSEC_SPEC_1683_HANDBAGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1683_HANDBAGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1693_HATS = "sec_spec_1693-Hats";
	@XmlElementDecl(name = SEC_SPEC_1693_HATS)
	public JAXBElement<String> createSEC_SPEC_1693_HATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1693_HATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1694_HOSIERY_AND_SOCKS = "sec_spec_1694-Hosiery_and_Socks";
	@XmlElementDecl(name = SEC_SPEC_1694_HOSIERY_AND_SOCKS)
	public JAXBElement<String> createSEC_SPEC_1694_HOSIERY_AND_SOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1694_HOSIERY_AND_SOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1695_SOCKS = "sec_spec_1695-Socks";
	@XmlElementDecl(name = SEC_SPEC_1695_SOCKS)
	public JAXBElement<String> createSEC_SPEC_1695_SOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1695_SOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE = "sec_spec_1696-Tights_and_Pantyhose";
	@XmlElementDecl(name = SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE)
	public JAXBElement<String> createSEC_SPEC_1696_TIGHTS_AND_PANTYHOSE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE), String.class, attribute);
	}

	public static final String SEC_SPEC_1697_LINGERIE_ACCESSORIES = "sec_spec_1697-Lingerie_Accessories";
	@XmlElementDecl(name = SEC_SPEC_1697_LINGERIE_ACCESSORIES)
	public JAXBElement<String> createSEC_SPEC_1697_LINGERIE_ACCESSORIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1697_LINGERIE_ACCESSORIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1698_SCARVES_AND_WRAPS = "sec_spec_1698-Scarves_and_Wraps";
	@XmlElementDecl(name = SEC_SPEC_1698_SCARVES_AND_WRAPS)
	public JAXBElement<String> createSEC_SPEC_1698_SCARVES_AND_WRAPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1698_SCARVES_AND_WRAPS), String.class, attribute);
	}

	public static final String SEC_SPEC_1699_BABY_AND_KIDS = "sec_spec_1699-Baby_and_Kids";
	@XmlElementDecl(name = SEC_SPEC_1699_BABY_AND_KIDS)
	public JAXBElement<String> createSEC_SPEC_1699_BABY_AND_KIDS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1699_BABY_AND_KIDS), String.class, attribute);
	}

	public static final String SEC_SPEC_1700_BABIES = "sec_spec_1700-Babies";
	@XmlElementDecl(name = SEC_SPEC_1700_BABIES)
	public JAXBElement<String> createSEC_SPEC_1700_BABIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1700_BABIES), String.class, attribute);
	}

	public static final String SEC_SPEC_1701_BABY_GEAR = "sec_spec_1701-Baby_Gear";
	@XmlElementDecl(name = SEC_SPEC_1701_BABY_GEAR)
	public JAXBElement<String> createSEC_SPEC_1701_BABY_GEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1701_BABY_GEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1703_BASSINETS = "sec_spec_1703-Bassinets";
	@XmlElementDecl(name = SEC_SPEC_1703_BASSINETS)
	public JAXBElement<String> createSEC_SPEC_1703_BASSINETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1703_BASSINETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1705_CAR_SEATS = "sec_spec_1705-Car_Seats";
	@XmlElementDecl(name = SEC_SPEC_1705_CAR_SEATS)
	public JAXBElement<String> createSEC_SPEC_1705_CAR_SEATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1705_CAR_SEATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1707_PLAYARDS = "sec_spec_1707-Playards";
	@XmlElementDecl(name = SEC_SPEC_1707_PLAYARDS)
	public JAXBElement<String> createSEC_SPEC_1707_PLAYARDS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1707_PLAYARDS), String.class, attribute);
	}

	public static final String SEC_SPEC_1708_STROLLERS = "sec_spec_1708-Strollers";
	@XmlElementDecl(name = SEC_SPEC_1708_STROLLERS)
	public JAXBElement<String> createSEC_SPEC_1708_STROLLERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1708_STROLLERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1709_BOYS = "sec_spec_1709-Boys";
	@XmlElementDecl(name = SEC_SPEC_1709_BOYS)
	public JAXBElement<String> createSEC_SPEC_1709_BOYS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1709_BOYS), String.class, attribute);
	}

	public static final String SEC_SPEC_1710_BOTTOMS = "sec_spec_1710-Bottoms";
	@XmlElementDecl(name = SEC_SPEC_1710_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_1710_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1710_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1711_COATS = "sec_spec_1711-Coats";
	@XmlElementDecl(name = SEC_SPEC_1711_COATS)
	public JAXBElement<String> createSEC_SPEC_1711_COATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1711_COATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1713_ONEPIECES = "sec_spec_1713-OnePieces";
	@XmlElementDecl(name = SEC_SPEC_1713_ONEPIECES)
	public JAXBElement<String> createSEC_SPEC_1713_ONEPIECES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1713_ONEPIECES), String.class, attribute);
	}

	public static final String SEC_SPEC_1714_SETS = "sec_spec_1714-Sets";
	@XmlElementDecl(name = SEC_SPEC_1714_SETS)
	public JAXBElement<String> createSEC_SPEC_1714_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1714_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1715_SLEEPWEAR = "sec_spec_1715-Sleepwear";
	@XmlElementDecl(name = SEC_SPEC_1715_SLEEPWEAR)
	public JAXBElement<String> createSEC_SPEC_1715_SLEEPWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1715_SLEEPWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1716_SWEATERS = "sec_spec_1716-Sweaters";
	@XmlElementDecl(name = SEC_SPEC_1716_SWEATERS)
	public JAXBElement<String> createSEC_SPEC_1716_SWEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1716_SWEATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1717_SWIMWEAR = "sec_spec_1717-Swimwear";
	@XmlElementDecl(name = SEC_SPEC_1717_SWIMWEAR)
	public JAXBElement<String> createSEC_SPEC_1717_SWIMWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1717_SWIMWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1718_TOPS_AND_TEES = "sec_spec_1718-Tops_and_Tees";
	@XmlElementDecl(name = SEC_SPEC_1718_TOPS_AND_TEES)
	public JAXBElement<String> createSEC_SPEC_1718_TOPS_AND_TEES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1718_TOPS_AND_TEES), String.class, attribute);
	}

	public static final String SEC_SPEC_1719_GIRLS = "sec_spec_1719-Girls";
	@XmlElementDecl(name = SEC_SPEC_1719_GIRLS)
	public JAXBElement<String> createSEC_SPEC_1719_GIRLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1719_GIRLS), String.class, attribute);
	}

	public static final String SEC_SPEC_1720_BOTTOMS = "sec_spec_1720-Bottoms";
	@XmlElementDecl(name = SEC_SPEC_1720_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_1720_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1720_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1721_COATS = "sec_spec_1721-Coats";
	@XmlElementDecl(name = SEC_SPEC_1721_COATS)
	public JAXBElement<String> createSEC_SPEC_1721_COATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1721_COATS), String.class, attribute);
	}

	public static final String SEC_SPEC_1724_ONEPIECES = "sec_spec_1724-OnePieces";
	@XmlElementDecl(name = SEC_SPEC_1724_ONEPIECES)
	public JAXBElement<String> createSEC_SPEC_1724_ONEPIECES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1724_ONEPIECES), String.class, attribute);
	}

	public static final String SEC_SPEC_1725_SETS = "sec_spec_1725-Sets";
	@XmlElementDecl(name = SEC_SPEC_1725_SETS)
	public JAXBElement<String> createSEC_SPEC_1725_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1725_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1726_SLEEPWEAR = "sec_spec_1726-Sleepwear";
	@XmlElementDecl(name = SEC_SPEC_1726_SLEEPWEAR)
	public JAXBElement<String> createSEC_SPEC_1726_SLEEPWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1726_SLEEPWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1727_SWEATERS = "sec_spec_1727-Sweaters";
	@XmlElementDecl(name = SEC_SPEC_1727_SWEATERS)
	public JAXBElement<String> createSEC_SPEC_1727_SWEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1727_SWEATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1728_SWIMWEAR = "sec_spec_1728-Swimwear";
	@XmlElementDecl(name = SEC_SPEC_1728_SWIMWEAR)
	public JAXBElement<String> createSEC_SPEC_1728_SWIMWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1728_SWIMWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1729_TOPS_AND_TEES = "sec_spec_1729-Tops_and_Tees";
	@XmlElementDecl(name = SEC_SPEC_1729_TOPS_AND_TEES)
	public JAXBElement<String> createSEC_SPEC_1729_TOPS_AND_TEES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1729_TOPS_AND_TEES), String.class, attribute);
	}

	public static final String SEC_SPEC_1730_NURSERY = "sec_spec_1730-Nursery";
	@XmlElementDecl(name = SEC_SPEC_1730_NURSERY)
	public JAXBElement<String> createSEC_SPEC_1730_NURSERY(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1730_NURSERY), String.class, attribute);
	}

	public static final String SEC_SPEC_1731_BEDDING = "sec_spec_1731-Bedding";
	@XmlElementDecl(name = SEC_SPEC_1731_BEDDING)
	public JAXBElement<String> createSEC_SPEC_1731_BEDDING(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1731_BEDDING), String.class, attribute);
	}

	public static final String SEC_SPEC_1732_BLANKETS = "sec_spec_1732-Blankets";
	@XmlElementDecl(name = SEC_SPEC_1732_BLANKETS)
	public JAXBElement<String> createSEC_SPEC_1732_BLANKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1732_BLANKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1736_DCOR = "sec_spec_1736-Dcor";
	@XmlElementDecl(name = SEC_SPEC_1736_DCOR)
	public JAXBElement<String> createSEC_SPEC_1736_DCOR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1736_DCOR), String.class, attribute);
	}

	public static final String SEC_SPEC_1738_BOYS = "sec_spec_1738-Boys";
	@XmlElementDecl(name = SEC_SPEC_1738_BOYS)
	public JAXBElement<String> createSEC_SPEC_1738_BOYS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1738_BOYS), String.class, attribute);
	}

	public static final String SEC_SPEC_1739_BOTTOMS = "sec_spec_1739-Bottoms";
	@XmlElementDecl(name = SEC_SPEC_1739_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_1739_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1739_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1740_JEANS = "sec_spec_1740-Jeans";
	@XmlElementDecl(name = SEC_SPEC_1740_JEANS)
	public JAXBElement<String> createSEC_SPEC_1740_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1740_JEANS), String.class, attribute);
	}

	public static final String SEC_SPEC_1742_SHORTS = "sec_spec_1742-Shorts";
	@XmlElementDecl(name = SEC_SPEC_1742_SHORTS)
	public JAXBElement<String> createSEC_SPEC_1742_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1742_SHORTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1743_COATS_AND_JACKETS = "sec_spec_1743-Coats_and_Jackets";
	@XmlElementDecl(name = SEC_SPEC_1743_COATS_AND_JACKETS)
	public JAXBElement<String> createSEC_SPEC_1743_COATS_AND_JACKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1743_COATS_AND_JACKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1745_PAJAMAS = "sec_spec_1745-Pajamas";
	@XmlElementDecl(name = SEC_SPEC_1745_PAJAMAS)
	public JAXBElement<String> createSEC_SPEC_1745_PAJAMAS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1745_PAJAMAS), String.class, attribute);
	}

	public static final String SEC_SPEC_1747_SETS = "sec_spec_1747-Sets";
	@XmlElementDecl(name = SEC_SPEC_1747_SETS)
	public JAXBElement<String> createSEC_SPEC_1747_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1747_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1748_SHIRTS_AND_TEES = "sec_spec_1748-Shirts_and_Tees";
	@XmlElementDecl(name = SEC_SPEC_1748_SHIRTS_AND_TEES)
	public JAXBElement<String> createSEC_SPEC_1748_SHIRTS_AND_TEES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1748_SHIRTS_AND_TEES), String.class, attribute);
	}

	public static final String SEC_SPEC_1749_SWEATERS = "sec_spec_1749-Sweaters";
	@XmlElementDecl(name = SEC_SPEC_1749_SWEATERS)
	public JAXBElement<String> createSEC_SPEC_1749_SWEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1749_SWEATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1750_SWIMWEAR = "sec_spec_1750-Swimwear";
	@XmlElementDecl(name = SEC_SPEC_1750_SWIMWEAR)
	public JAXBElement<String> createSEC_SPEC_1750_SWIMWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1750_SWIMWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1751_UNDERWEAR = "sec_spec_1751-Underwear";
	@XmlElementDecl(name = SEC_SPEC_1751_UNDERWEAR)
	public JAXBElement<String> createSEC_SPEC_1751_UNDERWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1751_UNDERWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1752_GIRLS = "sec_spec_1752-Girls";
	@XmlElementDecl(name = SEC_SPEC_1752_GIRLS)
	public JAXBElement<String> createSEC_SPEC_1752_GIRLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1752_GIRLS), String.class, attribute);
	}

	public static final String SEC_SPEC_1753_BOTTOMS = "sec_spec_1753-Bottoms";
	@XmlElementDecl(name = SEC_SPEC_1753_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_1753_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1753_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1754_JEANS = "sec_spec_1754-Jeans";
	@XmlElementDecl(name = SEC_SPEC_1754_JEANS)
	public JAXBElement<String> createSEC_SPEC_1754_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1754_JEANS), String.class, attribute);
	}

	public static final String SEC_SPEC_1756_SHORTS = "sec_spec_1756-Shorts";
	@XmlElementDecl(name = SEC_SPEC_1756_SHORTS)
	public JAXBElement<String> createSEC_SPEC_1756_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1756_SHORTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1757_SKIRTS = "sec_spec_1757-Skirts";
	@XmlElementDecl(name = SEC_SPEC_1757_SKIRTS)
	public JAXBElement<String> createSEC_SPEC_1757_SKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1757_SKIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1758_COATS_AND_JACKETS = "sec_spec_1758-Coats_and_Jackets";
	@XmlElementDecl(name = SEC_SPEC_1758_COATS_AND_JACKETS)
	public JAXBElement<String> createSEC_SPEC_1758_COATS_AND_JACKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1758_COATS_AND_JACKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1759_DRESSES = "sec_spec_1759-Dresses";
	@XmlElementDecl(name = SEC_SPEC_1759_DRESSES)
	public JAXBElement<String> createSEC_SPEC_1759_DRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1759_DRESSES), String.class, attribute);
	}

	public static final String SEC_SPEC_1760_PAJAMAS = "sec_spec_1760-Pajamas";
	@XmlElementDecl(name = SEC_SPEC_1760_PAJAMAS)
	public JAXBElement<String> createSEC_SPEC_1760_PAJAMAS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1760_PAJAMAS), String.class, attribute);
	}

	public static final String SEC_SPEC_1761_SCHOOL_UNIFORMS = "sec_spec_1761-School_Uniforms";
	@XmlElementDecl(name = SEC_SPEC_1761_SCHOOL_UNIFORMS)
	public JAXBElement<String> createSEC_SPEC_1761_SCHOOL_UNIFORMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1761_SCHOOL_UNIFORMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1762_SETS = "sec_spec_1762-Sets";
	@XmlElementDecl(name = SEC_SPEC_1762_SETS)
	public JAXBElement<String> createSEC_SPEC_1762_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1762_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1763_SHIRTS_AND_TEES = "sec_spec_1763-Shirts_and_Tees";
	@XmlElementDecl(name = SEC_SPEC_1763_SHIRTS_AND_TEES)
	public JAXBElement<String> createSEC_SPEC_1763_SHIRTS_AND_TEES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1763_SHIRTS_AND_TEES), String.class, attribute);
	}

	public static final String SEC_SPEC_1764_SWEATERS = "sec_spec_1764-Sweaters";
	@XmlElementDecl(name = SEC_SPEC_1764_SWEATERS)
	public JAXBElement<String> createSEC_SPEC_1764_SWEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1764_SWEATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1765_SWIMWEAR = "sec_spec_1765-Swimwear";
	@XmlElementDecl(name = SEC_SPEC_1765_SWIMWEAR)
	public JAXBElement<String> createSEC_SPEC_1765_SWIMWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1765_SWIMWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1766_UNDERWEAR = "sec_spec_1766-Underwear";
	@XmlElementDecl(name = SEC_SPEC_1766_UNDERWEAR)
	public JAXBElement<String> createSEC_SPEC_1766_UNDERWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1766_UNDERWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_1767_TOYS = "sec_spec_1767-Toys";
	@XmlElementDecl(name = SEC_SPEC_1767_TOYS)
	public JAXBElement<String> createSEC_SPEC_1767_TOYS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1767_TOYS), String.class, attribute);
	}

	public static final String SEC_SPEC_1774_BEAUTY = "sec_spec_1774-Beauty";
	@XmlElementDecl(name = SEC_SPEC_1774_BEAUTY)
	public JAXBElement<String> createSEC_SPEC_1774_BEAUTY(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1774_BEAUTY), String.class, attribute);
	}

	public static final String SEC_SPEC_1775_BATH_AND_BODY = "sec_spec_1775-Bath_and_Body";
	@XmlElementDecl(name = SEC_SPEC_1775_BATH_AND_BODY)
	public JAXBElement<String> createSEC_SPEC_1775_BATH_AND_BODY(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1775_BATH_AND_BODY), String.class, attribute);
	}

	public static final String SEC_SPEC_1779_GIFT_AND_VALUE_SETS = "sec_spec_1779-Gift_and_Value_Sets";
	@XmlElementDecl(name = SEC_SPEC_1779_GIFT_AND_VALUE_SETS)
	public JAXBElement<String> createSEC_SPEC_1779_GIFT_AND_VALUE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1779_GIFT_AND_VALUE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1781_MOISTURIZERS = "sec_spec_1781-Moisturizers";
	@XmlElementDecl(name = SEC_SPEC_1781_MOISTURIZERS)
	public JAXBElement<String> createSEC_SPEC_1781_MOISTURIZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1781_MOISTURIZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1785_SUN_CARE = "sec_spec_1785-Sun_Care";
	@XmlElementDecl(name = SEC_SPEC_1785_SUN_CARE)
	public JAXBElement<String> createSEC_SPEC_1785_SUN_CARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1785_SUN_CARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1786_FRAGRANCE = "sec_spec_1786-Fragrance";
	@XmlElementDecl(name = SEC_SPEC_1786_FRAGRANCE)
	public JAXBElement<String> createSEC_SPEC_1786_FRAGRANCE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1786_FRAGRANCE), String.class, attribute);
	}

	public static final String SEC_SPEC_1788_GIFT_AND_VALUE_SETS = "sec_spec_1788-Gift_and_Value_Sets";
	@XmlElementDecl(name = SEC_SPEC_1788_GIFT_AND_VALUE_SETS)
	public JAXBElement<String> createSEC_SPEC_1788_GIFT_AND_VALUE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1788_GIFT_AND_VALUE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1795_HAIR_CARE = "sec_spec_1795-Hair_Care";
	@XmlElementDecl(name = SEC_SPEC_1795_HAIR_CARE)
	public JAXBElement<String> createSEC_SPEC_1795_HAIR_CARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1795_HAIR_CARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1796_CONDITIONER = "sec_spec_1796-Conditioner";
	@XmlElementDecl(name = SEC_SPEC_1796_CONDITIONER)
	public JAXBElement<String> createSEC_SPEC_1796_CONDITIONER(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1796_CONDITIONER), String.class, attribute);
	}

	public static final String SEC_SPEC_1797_SHAMPOO = "sec_spec_1797-Shampoo";
	@XmlElementDecl(name = SEC_SPEC_1797_SHAMPOO)
	public JAXBElement<String> createSEC_SPEC_1797_SHAMPOO(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1797_SHAMPOO), String.class, attribute);
	}

	public static final String SEC_SPEC_1798_STYLING_PRODUCTS = "sec_spec_1798-Styling_Products";
	@XmlElementDecl(name = SEC_SPEC_1798_STYLING_PRODUCTS)
	public JAXBElement<String> createSEC_SPEC_1798_STYLING_PRODUCTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1798_STYLING_PRODUCTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1804_TREATMENTS = "sec_spec_1804-Treatments";
	@XmlElementDecl(name = SEC_SPEC_1804_TREATMENTS)
	public JAXBElement<String> createSEC_SPEC_1804_TREATMENTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1804_TREATMENTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1805_MAKEUP = "sec_spec_1805-Makeup";
	@XmlElementDecl(name = SEC_SPEC_1805_MAKEUP)
	public JAXBElement<String> createSEC_SPEC_1805_MAKEUP(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1805_MAKEUP), String.class, attribute);
	}

	public static final String SEC_SPEC_1812_MASCARA = "sec_spec_1812-Mascara";
	@XmlElementDecl(name = SEC_SPEC_1812_MASCARA)
	public JAXBElement<String> createSEC_SPEC_1812_MASCARA(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1812_MASCARA), String.class, attribute);
	}

	public static final String SEC_SPEC_1813_FACE = "sec_spec_1813-Face";
	@XmlElementDecl(name = SEC_SPEC_1813_FACE)
	public JAXBElement<String> createSEC_SPEC_1813_FACE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1813_FACE), String.class, attribute);
	}

	public static final String SEC_SPEC_1817_FOUNDATION = "sec_spec_1817-Foundation";
	@XmlElementDecl(name = SEC_SPEC_1817_FOUNDATION)
	public JAXBElement<String> createSEC_SPEC_1817_FOUNDATION(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1817_FOUNDATION), String.class, attribute);
	}

	public static final String SEC_SPEC_1822_LIP_BALM = "sec_spec_1822-Lip_Balm";
	@XmlElementDecl(name = SEC_SPEC_1822_LIP_BALM)
	public JAXBElement<String> createSEC_SPEC_1822_LIP_BALM(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1822_LIP_BALM), String.class, attribute);
	}

	public static final String SEC_SPEC_1827_LIPSTICK = "sec_spec_1827-Lipstick";
	@XmlElementDecl(name = SEC_SPEC_1827_LIPSTICK)
	public JAXBElement<String> createSEC_SPEC_1827_LIPSTICK(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1827_LIPSTICK), String.class, attribute);
	}

	public static final String SEC_SPEC_1828_PALETTES_AND_VALUE_SETS = "sec_spec_1828-Palettes_and_Value_Sets";
	@XmlElementDecl(name = SEC_SPEC_1828_PALETTES_AND_VALUE_SETS)
	public JAXBElement<String> createSEC_SPEC_1828_PALETTES_AND_VALUE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1828_PALETTES_AND_VALUE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1833_PALETTES_AND_VALUE_SETS = "sec_spec_1833-Palettes_and_Value_Sets";
	@XmlElementDecl(name = SEC_SPEC_1833_PALETTES_AND_VALUE_SETS)
	public JAXBElement<String> createSEC_SPEC_1833_PALETTES_AND_VALUE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1833_PALETTES_AND_VALUE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1834_SKIN_CARE = "sec_spec_1834-Skin_Care";
	@XmlElementDecl(name = SEC_SPEC_1834_SKIN_CARE)
	public JAXBElement<String> createSEC_SPEC_1834_SKIN_CARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1834_SKIN_CARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1840_GIFT_AND_VALUE_SETS = "sec_spec_1840-Gift_and_Value_Sets";
	@XmlElementDecl(name = SEC_SPEC_1840_GIFT_AND_VALUE_SETS)
	public JAXBElement<String> createSEC_SPEC_1840_GIFT_AND_VALUE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1840_GIFT_AND_VALUE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1841_MOISTURIZING = "sec_spec_1841-Moisturizing";
	@XmlElementDecl(name = SEC_SPEC_1841_MOISTURIZING)
	public JAXBElement<String> createSEC_SPEC_1841_MOISTURIZING(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1841_MOISTURIZING), String.class, attribute);
	}

	public static final String SEC_SPEC_1847_SHAVING_AND_GROOMING = "sec_spec_1847-Shaving_and_Grooming";
	@XmlElementDecl(name = SEC_SPEC_1847_SHAVING_AND_GROOMING)
	public JAXBElement<String> createSEC_SPEC_1847_SHAVING_AND_GROOMING(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1847_SHAVING_AND_GROOMING), String.class, attribute);
	}

	public static final String SEC_SPEC_1869_FOR_THE_HOME = "sec_spec_1869-For_the_Home";
	@XmlElementDecl(name = SEC_SPEC_1869_FOR_THE_HOME)
	public JAXBElement<String> createSEC_SPEC_1869_FOR_THE_HOME(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1869_FOR_THE_HOME), String.class, attribute);
	}

	public static final String SEC_SPEC_1870_BED_AND_BATH = "sec_spec_1870-Bed_and_Bath";
	@XmlElementDecl(name = SEC_SPEC_1870_BED_AND_BATH)
	public JAXBElement<String> createSEC_SPEC_1870_BED_AND_BATH(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1870_BED_AND_BATH), String.class, attribute);
	}

	public static final String SEC_SPEC_1872_BATH_ACCESSORY_SETS = "sec_spec_1872-Bath_Accessory_Sets";
	@XmlElementDecl(name = SEC_SPEC_1872_BATH_ACCESSORY_SETS)
	public JAXBElement<String> createSEC_SPEC_1872_BATH_ACCESSORY_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1872_BATH_ACCESSORY_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1873_BATH_MATS_AND_RUGS = "sec_spec_1873-Bath_Mats_and_Rugs";
	@XmlElementDecl(name = SEC_SPEC_1873_BATH_MATS_AND_RUGS)
	public JAXBElement<String> createSEC_SPEC_1873_BATH_MATS_AND_RUGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1873_BATH_MATS_AND_RUGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1875_BATH_TOWELS = "sec_spec_1875-Bath_Towels";
	@XmlElementDecl(name = SEC_SPEC_1875_BATH_TOWELS)
	public JAXBElement<String> createSEC_SPEC_1875_BATH_TOWELS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1875_BATH_TOWELS), String.class, attribute);
	}

	public static final String SEC_SPEC_1877_SHOWER_CURTAINS = "sec_spec_1877-Shower_Curtains";
	@XmlElementDecl(name = SEC_SPEC_1877_SHOWER_CURTAINS)
	public JAXBElement<String> createSEC_SPEC_1877_SHOWER_CURTAINS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1877_SHOWER_CURTAINS), String.class, attribute);
	}

	public static final String SEC_SPEC_1878_BEDDING = "sec_spec_1878-Bedding";
	@XmlElementDecl(name = SEC_SPEC_1878_BEDDING)
	public JAXBElement<String> createSEC_SPEC_1878_BEDDING(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1878_BEDDING), String.class, attribute);
	}

	public static final String SEC_SPEC_1880_BEDSKIRTS = "sec_spec_1880-Bedskirts";
	@XmlElementDecl(name = SEC_SPEC_1880_BEDSKIRTS)
	public JAXBElement<String> createSEC_SPEC_1880_BEDSKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1880_BEDSKIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1882_BLANKETS = "sec_spec_1882-Blankets";
	@XmlElementDecl(name = SEC_SPEC_1882_BLANKETS)
	public JAXBElement<String> createSEC_SPEC_1882_BLANKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1882_BLANKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS = "sec_spec_1886-Mattress_Pads_and_Toppers";
	@XmlElementDecl(name = SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS)
	public JAXBElement<String> createSEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1887_PILLOWS = "sec_spec_1887-Pillows";
	@XmlElementDecl(name = SEC_SPEC_1887_PILLOWS)
	public JAXBElement<String> createSEC_SPEC_1887_PILLOWS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1887_PILLOWS), String.class, attribute);
	}

	public static final String SEC_SPEC_1889_SHEETS = "sec_spec_1889-Sheets";
	@XmlElementDecl(name = SEC_SPEC_1889_SHEETS)
	public JAXBElement<String> createSEC_SPEC_1889_SHEETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1889_SHEETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1892_GARMENT_STEAMERS = "sec_spec_1892-Garment_Steamers";
	@XmlElementDecl(name = SEC_SPEC_1892_GARMENT_STEAMERS)
	public JAXBElement<String> createSEC_SPEC_1892_GARMENT_STEAMERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1892_GARMENT_STEAMERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1893_HOME_ORGANIZATION = "sec_spec_1893-Home_Organization";
	@XmlElementDecl(name = SEC_SPEC_1893_HOME_ORGANIZATION)
	public JAXBElement<String> createSEC_SPEC_1893_HOME_ORGANIZATION(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1893_HOME_ORGANIZATION), String.class, attribute);
	}

	public static final String SEC_SPEC_1895_IRONS = "sec_spec_1895-Irons";
	@XmlElementDecl(name = SEC_SPEC_1895_IRONS)
	public JAXBElement<String> createSEC_SPEC_1895_IRONS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1895_IRONS), String.class, attribute);
	}

	public static final String SEC_SPEC_1896_LAUNDRY_HAMPERS = "sec_spec_1896-Laundry_Hampers";
	@XmlElementDecl(name = SEC_SPEC_1896_LAUNDRY_HAMPERS)
	public JAXBElement<String> createSEC_SPEC_1896_LAUNDRY_HAMPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1896_LAUNDRY_HAMPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1898_VACUUMS = "sec_spec_1898-Vacuums";
	@XmlElementDecl(name = SEC_SPEC_1898_VACUUMS)
	public JAXBElement<String> createSEC_SPEC_1898_VACUUMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1898_VACUUMS), String.class, attribute);
	}

	public static final String SEC_SPEC_1899_DINING = "sec_spec_1899-Dining";
	@XmlElementDecl(name = SEC_SPEC_1899_DINING)
	public JAXBElement<String> createSEC_SPEC_1899_DINING(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1899_DINING), String.class, attribute);
	}

	public static final String SEC_SPEC_1901_BARWARE_SETS = "sec_spec_1901-Barware_Sets";
	@XmlElementDecl(name = SEC_SPEC_1901_BARWARE_SETS)
	public JAXBElement<String> createSEC_SPEC_1901_BARWARE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1901_BARWARE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1904_COASTERS = "sec_spec_1904-Coasters";
	@XmlElementDecl(name = SEC_SPEC_1904_COASTERS)
	public JAXBElement<String> createSEC_SPEC_1904_COASTERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1904_COASTERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1906_WINE_OPENERS = "sec_spec_1906-Wine_Openers";
	@XmlElementDecl(name = SEC_SPEC_1906_WINE_OPENERS)
	public JAXBElement<String> createSEC_SPEC_1906_WINE_OPENERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1906_WINE_OPENERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1907_WINE_RACKS = "sec_spec_1907-Wine_Racks";
	@XmlElementDecl(name = SEC_SPEC_1907_WINE_RACKS)
	public JAXBElement<String> createSEC_SPEC_1907_WINE_RACKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1907_WINE_RACKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1908_DINNERWARE = "sec_spec_1908-Dinnerware";
	@XmlElementDecl(name = SEC_SPEC_1908_DINNERWARE)
	public JAXBElement<String> createSEC_SPEC_1908_DINNERWARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1908_DINNERWARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1912_DINNER_PLATES = "sec_spec_1912-Dinner_Plates";
	@XmlElementDecl(name = SEC_SPEC_1912_DINNER_PLATES)
	public JAXBElement<String> createSEC_SPEC_1912_DINNER_PLATES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1912_DINNER_PLATES), String.class, attribute);
	}

	public static final String SEC_SPEC_1913_DINNERWARE_SETS = "sec_spec_1913-Dinnerware_Sets";
	@XmlElementDecl(name = SEC_SPEC_1913_DINNERWARE_SETS)
	public JAXBElement<String> createSEC_SPEC_1913_DINNERWARE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1913_DINNERWARE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1914_KIDS_DINNERWARE = "sec_spec_1914-Kids_Dinnerware";
	@XmlElementDecl(name = SEC_SPEC_1914_KIDS_DINNERWARE)
	public JAXBElement<String> createSEC_SPEC_1914_KIDS_DINNERWARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1914_KIDS_DINNERWARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1916_DRINKWARE = "sec_spec_1916-Drinkware";
	@XmlElementDecl(name = SEC_SPEC_1916_DRINKWARE)
	public JAXBElement<String> createSEC_SPEC_1916_DRINKWARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1916_DRINKWARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1917_BEER_GLASSES_AND_MUGS = "sec_spec_1917-Beer_Glasses_and_Mugs";
	@XmlElementDecl(name = SEC_SPEC_1917_BEER_GLASSES_AND_MUGS)
	public JAXBElement<String> createSEC_SPEC_1917_BEER_GLASSES_AND_MUGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1917_BEER_GLASSES_AND_MUGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1918_COFFEE_MUGS = "sec_spec_1918-Coffee_Mugs";
	@XmlElementDecl(name = SEC_SPEC_1918_COFFEE_MUGS)
	public JAXBElement<String> createSEC_SPEC_1918_COFFEE_MUGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1918_COFFEE_MUGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1919_COFFEE_TUMBLERS = "sec_spec_1919-Coffee_Tumblers";
	@XmlElementDecl(name = SEC_SPEC_1919_COFFEE_TUMBLERS)
	public JAXBElement<String> createSEC_SPEC_1919_COFFEE_TUMBLERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1919_COFFEE_TUMBLERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1920_DRINKWARE_SETS = "sec_spec_1920-Drinkware_Sets";
	@XmlElementDecl(name = SEC_SPEC_1920_DRINKWARE_SETS)
	public JAXBElement<String> createSEC_SPEC_1920_DRINKWARE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1920_DRINKWARE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1923_MASON_JARS = "sec_spec_1923-Mason_Jars";
	@XmlElementDecl(name = SEC_SPEC_1923_MASON_JARS)
	public JAXBElement<String> createSEC_SPEC_1923_MASON_JARS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1923_MASON_JARS), String.class, attribute);
	}

	public static final String SEC_SPEC_1925_WATER_BOTTLES = "sec_spec_1925-Water_Bottles";
	@XmlElementDecl(name = SEC_SPEC_1925_WATER_BOTTLES)
	public JAXBElement<String> createSEC_SPEC_1925_WATER_BOTTLES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1925_WATER_BOTTLES), String.class, attribute);
	}

	public static final String SEC_SPEC_1926_WINE_GLASSES = "sec_spec_1926-Wine_Glasses";
	@XmlElementDecl(name = SEC_SPEC_1926_WINE_GLASSES)
	public JAXBElement<String> createSEC_SPEC_1926_WINE_GLASSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1926_WINE_GLASSES), String.class, attribute);
	}

	public static final String SEC_SPEC_1927_FLATWARE = "sec_spec_1927-Flatware";
	@XmlElementDecl(name = SEC_SPEC_1927_FLATWARE)
	public JAXBElement<String> createSEC_SPEC_1927_FLATWARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1927_FLATWARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1928_FLATWARE_SETS = "sec_spec_1928-Flatware_Sets";
	@XmlElementDecl(name = SEC_SPEC_1928_FLATWARE_SETS)
	public JAXBElement<String> createSEC_SPEC_1928_FLATWARE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1928_FLATWARE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1932_BEVERAGE_DISPENSERS = "sec_spec_1932-Beverage_Dispensers";
	@XmlElementDecl(name = SEC_SPEC_1932_BEVERAGE_DISPENSERS)
	public JAXBElement<String> createSEC_SPEC_1932_BEVERAGE_DISPENSERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1932_BEVERAGE_DISPENSERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1934_CARAFES_AND_DECANTERS = "sec_spec_1934-Carafes_and_Decanters";
	@XmlElementDecl(name = SEC_SPEC_1934_CARAFES_AND_DECANTERS)
	public JAXBElement<String> createSEC_SPEC_1934_CARAFES_AND_DECANTERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1934_CARAFES_AND_DECANTERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1937_DIVIDED_SERVERS = "sec_spec_1937-Divided_Servers";
	@XmlElementDecl(name = SEC_SPEC_1937_DIVIDED_SERVERS)
	public JAXBElement<String> createSEC_SPEC_1937_DIVIDED_SERVERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1937_DIVIDED_SERVERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1938_PITCHERS = "sec_spec_1938-Pitchers";
	@XmlElementDecl(name = SEC_SPEC_1938_PITCHERS)
	public JAXBElement<String> createSEC_SPEC_1938_PITCHERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1938_PITCHERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1941_SERVING_BOWLS = "sec_spec_1941-Serving_Bowls";
	@XmlElementDecl(name = SEC_SPEC_1941_SERVING_BOWLS)
	public JAXBElement<String> createSEC_SPEC_1941_SERVING_BOWLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1941_SERVING_BOWLS), String.class, attribute);
	}

	public static final String SEC_SPEC_1942_SERVING_UTENSILS_AND_SETS = "sec_spec_1942-Serving_Utensils_and_Sets";
	@XmlElementDecl(name = SEC_SPEC_1942_SERVING_UTENSILS_AND_SETS)
	public JAXBElement<String> createSEC_SPEC_1942_SERVING_UTENSILS_AND_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1942_SERVING_UTENSILS_AND_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1945_CHILI_POTS = "sec_spec_1945-Chili_Pots";
	@XmlElementDecl(name = SEC_SPEC_1945_CHILI_POTS)
	public JAXBElement<String> createSEC_SPEC_1945_CHILI_POTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1945_CHILI_POTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1947_PIZZA_TRAYS = "sec_spec_1947-Pizza_Trays";
	@XmlElementDecl(name = SEC_SPEC_1947_PIZZA_TRAYS)
	public JAXBElement<String> createSEC_SPEC_1947_PIZZA_TRAYS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1947_PIZZA_TRAYS), String.class, attribute);
	}

	public static final String SEC_SPEC_1948_TIERED_SERVERS = "sec_spec_1948-Tiered_Servers";
	@XmlElementDecl(name = SEC_SPEC_1948_TIERED_SERVERS)
	public JAXBElement<String> createSEC_SPEC_1948_TIERED_SERVERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1948_TIERED_SERVERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1949_TABLE_LINENS = "sec_spec_1949-Table_Linens";
	@XmlElementDecl(name = SEC_SPEC_1949_TABLE_LINENS)
	public JAXBElement<String> createSEC_SPEC_1949_TABLE_LINENS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1949_TABLE_LINENS), String.class, attribute);
	}

	public static final String SEC_SPEC_1956_ELECTRONICS = "sec_spec_1956-Electronics";
	@XmlElementDecl(name = SEC_SPEC_1956_ELECTRONICS)
	public JAXBElement<String> createSEC_SPEC_1956_ELECTRONICS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1956_ELECTRONICS), String.class, attribute);
	}

	public static final String SEC_SPEC_1957_ADAPTERS = "sec_spec_1957-Adapters";
	@XmlElementDecl(name = SEC_SPEC_1957_ADAPTERS)
	public JAXBElement<String> createSEC_SPEC_1957_ADAPTERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1957_ADAPTERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1959_CLOCKS = "sec_spec_1959-Clocks";
	@XmlElementDecl(name = SEC_SPEC_1959_CLOCKS)
	public JAXBElement<String> createSEC_SPEC_1959_CLOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1959_CLOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_1960_FANS_AND_HEATERS = "sec_spec_1960-Fans_and_Heaters";
	@XmlElementDecl(name = SEC_SPEC_1960_FANS_AND_HEATERS)
	public JAXBElement<String> createSEC_SPEC_1960_FANS_AND_HEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1960_FANS_AND_HEATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1961_HEADPHONES = "sec_spec_1961-Headphones";
	@XmlElementDecl(name = SEC_SPEC_1961_HEADPHONES)
	public JAXBElement<String> createSEC_SPEC_1961_HEADPHONES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1961_HEADPHONES), String.class, attribute);
	}

	public static final String SEC_SPEC_1964_PORTABLE_CHARGERS = "sec_spec_1964-Portable_Chargers";
	@XmlElementDecl(name = SEC_SPEC_1964_PORTABLE_CHARGERS)
	public JAXBElement<String> createSEC_SPEC_1964_PORTABLE_CHARGERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1964_PORTABLE_CHARGERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1966_SPEAKERS = "sec_spec_1966-Speakers";
	@XmlElementDecl(name = SEC_SPEC_1966_SPEAKERS)
	public JAXBElement<String> createSEC_SPEC_1966_SPEAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1966_SPEAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_1967_TABLET_STANDS_AND_CASES = "sec_spec_1967-Tablet_Stands_and_Cases";
	@XmlElementDecl(name = SEC_SPEC_1967_TABLET_STANDS_AND_CASES)
	public JAXBElement<String> createSEC_SPEC_1967_TABLET_STANDS_AND_CASES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1967_TABLET_STANDS_AND_CASES), String.class, attribute);
	}

	public static final String SEC_SPEC_1970_FURNITURE = "sec_spec_1970-Furniture";
	@XmlElementDecl(name = SEC_SPEC_1970_FURNITURE)
	public JAXBElement<String> createSEC_SPEC_1970_FURNITURE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1970_FURNITURE), String.class, attribute);
	}

	public static final String SEC_SPEC_1975_GOURMET_FOOD_AND_GIFTS = "sec_spec_1975-Gourmet_Food_and_Gifts";
	@XmlElementDecl(name = SEC_SPEC_1975_GOURMET_FOOD_AND_GIFTS)
	public JAXBElement<String> createSEC_SPEC_1975_GOURMET_FOOD_AND_GIFTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1975_GOURMET_FOOD_AND_GIFTS), String.class, attribute);
	}

	public static final String SEC_SPEC_1976_HOME_DCOR = "sec_spec_1976-Home_Dcor";
	@XmlElementDecl(name = SEC_SPEC_1976_HOME_DCOR)
	public JAXBElement<String> createSEC_SPEC_1976_HOME_DCOR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1976_HOME_DCOR), String.class, attribute);
	}

	public static final String SEC_SPEC_1979_CANDLES = "sec_spec_1979-Candles";
	@XmlElementDecl(name = SEC_SPEC_1979_CANDLES)
	public JAXBElement<String> createSEC_SPEC_1979_CANDLES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1979_CANDLES), String.class, attribute);
	}

	public static final String SEC_SPEC_1981_FLAMELESS_CANDLES = "sec_spec_1981-Flameless_Candles";
	@XmlElementDecl(name = SEC_SPEC_1981_FLAMELESS_CANDLES)
	public JAXBElement<String> createSEC_SPEC_1981_FLAMELESS_CANDLES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1981_FLAMELESS_CANDLES), String.class, attribute);
	}

	public static final String SEC_SPEC_1982_POTPOURRI = "sec_spec_1982-Potpourri";
	@XmlElementDecl(name = SEC_SPEC_1982_POTPOURRI)
	public JAXBElement<String> createSEC_SPEC_1982_POTPOURRI(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1982_POTPOURRI), String.class, attribute);
	}

	public static final String SEC_SPEC_1983_SCENT_PLUGS = "sec_spec_1983-Scent_Plugs";
	@XmlElementDecl(name = SEC_SPEC_1983_SCENT_PLUGS)
	public JAXBElement<String> createSEC_SPEC_1983_SCENT_PLUGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1983_SCENT_PLUGS), String.class, attribute);
	}

	public static final String SEC_SPEC_1984_WARMERS_AND_MELTCUPS = "sec_spec_1984-Warmers_and_Meltcups";
	@XmlElementDecl(name = SEC_SPEC_1984_WARMERS_AND_MELTCUPS)
	public JAXBElement<String> createSEC_SPEC_1984_WARMERS_AND_MELTCUPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1984_WARMERS_AND_MELTCUPS), String.class, attribute);
	}

	public static final String SEC_SPEC_1986_DECORATIVE_BASKETS = "sec_spec_1986-Decorative_Baskets";
	@XmlElementDecl(name = SEC_SPEC_1986_DECORATIVE_BASKETS)
	public JAXBElement<String> createSEC_SPEC_1986_DECORATIVE_BASKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1986_DECORATIVE_BASKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1988_FRAMES = "sec_spec_1988-Frames";
	@XmlElementDecl(name = SEC_SPEC_1988_FRAMES)
	public JAXBElement<String> createSEC_SPEC_1988_FRAMES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1988_FRAMES), String.class, attribute);
	}

	public static final String SEC_SPEC_1989_MIRRORS = "sec_spec_1989-Mirrors";
	@XmlElementDecl(name = SEC_SPEC_1989_MIRRORS)
	public JAXBElement<String> createSEC_SPEC_1989_MIRRORS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1989_MIRRORS), String.class, attribute);
	}

	public static final String SEC_SPEC_1991_THROW_PILLOWS = "sec_spec_1991-Throw_Pillows";
	@XmlElementDecl(name = SEC_SPEC_1991_THROW_PILLOWS)
	public JAXBElement<String> createSEC_SPEC_1991_THROW_PILLOWS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1991_THROW_PILLOWS), String.class, attribute);
	}

	public static final String SEC_SPEC_1993_WALL_ART = "sec_spec_1993-Wall_Art";
	@XmlElementDecl(name = SEC_SPEC_1993_WALL_ART)
	public JAXBElement<String> createSEC_SPEC_1993_WALL_ART(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1993_WALL_ART), String.class, attribute);
	}

	public static final String SEC_SPEC_1994_KITCHEN = "sec_spec_1994-Kitchen";
	@XmlElementDecl(name = SEC_SPEC_1994_KITCHEN)
	public JAXBElement<String> createSEC_SPEC_1994_KITCHEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1994_KITCHEN), String.class, attribute);
	}

	public static final String SEC_SPEC_1995_BAKEWARE = "sec_spec_1995-Bakeware";
	@XmlElementDecl(name = SEC_SPEC_1995_BAKEWARE)
	public JAXBElement<String> createSEC_SPEC_1995_BAKEWARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1995_BAKEWARE), String.class, attribute);
	}

	public static final String SEC_SPEC_1996_BAKEWARE_SETS = "sec_spec_1996-Bakeware_Sets";
	@XmlElementDecl(name = SEC_SPEC_1996_BAKEWARE_SETS)
	public JAXBElement<String> createSEC_SPEC_1996_BAKEWARE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1996_BAKEWARE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_1997_BAKING_DISHES = "sec_spec_1997-Baking_Dishes";
	@XmlElementDecl(name = SEC_SPEC_1997_BAKING_DISHES)
	public JAXBElement<String> createSEC_SPEC_1997_BAKING_DISHES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1997_BAKING_DISHES), String.class, attribute);
	}

	public static final String SEC_SPEC_1998_BAKING_SHEETS = "sec_spec_1998-Baking_Sheets";
	@XmlElementDecl(name = SEC_SPEC_1998_BAKING_SHEETS)
	public JAXBElement<String> createSEC_SPEC_1998_BAKING_SHEETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_1998_BAKING_SHEETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2000_CAKE_PANS = "sec_spec_2000-Cake_Pans";
	@XmlElementDecl(name = SEC_SPEC_2000_CAKE_PANS)
	public JAXBElement<String> createSEC_SPEC_2000_CAKE_PANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2000_CAKE_PANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2002_CUPCAKE_AND_MUFFIN_PANS = "sec_spec_2002-Cupcake_and_Muffin_Pans";
	@XmlElementDecl(name = SEC_SPEC_2002_CUPCAKE_AND_MUFFIN_PANS)
	public JAXBElement<String> createSEC_SPEC_2002_CUPCAKE_AND_MUFFIN_PANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2002_CUPCAKE_AND_MUFFIN_PANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2004_PIZZA_PANS_AND_STONES = "sec_spec_2004-Pizza_Pans_and_Stones";
	@XmlElementDecl(name = SEC_SPEC_2004_PIZZA_PANS_AND_STONES)
	public JAXBElement<String> createSEC_SPEC_2004_PIZZA_PANS_AND_STONES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2004_PIZZA_PANS_AND_STONES), String.class, attribute);
	}

	public static final String SEC_SPEC_2005_SPECIALTY_PANS = "sec_spec_2005-Specialty_Pans";
	@XmlElementDecl(name = SEC_SPEC_2005_SPECIALTY_PANS)
	public JAXBElement<String> createSEC_SPEC_2005_SPECIALTY_PANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2005_SPECIALTY_PANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2007_COFFEE_AND_TEA_STORAGE = "sec_spec_2007-Coffee_and_Tea_Storage";
	@XmlElementDecl(name = SEC_SPEC_2007_COFFEE_AND_TEA_STORAGE)
	public JAXBElement<String> createSEC_SPEC_2007_COFFEE_AND_TEA_STORAGE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2007_COFFEE_AND_TEA_STORAGE), String.class, attribute);
	}

	public static final String SEC_SPEC_2009_CARAFES_AND_AIRPOTS = "sec_spec_2009-Carafes_and_Airpots";
	@XmlElementDecl(name = SEC_SPEC_2009_CARAFES_AND_AIRPOTS)
	public JAXBElement<String> createSEC_SPEC_2009_CARAFES_AND_AIRPOTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2009_CARAFES_AND_AIRPOTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2010_FILTERS_AND_REPLACEMENT_PARTS = "sec_spec_2010-Filters_and_Replacement_Parts";
	@XmlElementDecl(name = SEC_SPEC_2010_FILTERS_AND_REPLACEMENT_PARTS)
	public JAXBElement<String> createSEC_SPEC_2010_FILTERS_AND_REPLACEMENT_PARTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2010_FILTERS_AND_REPLACEMENT_PARTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2011_FRENCH_PRESSES = "sec_spec_2011-French_Presses";
	@XmlElementDecl(name = SEC_SPEC_2011_FRENCH_PRESSES)
	public JAXBElement<String> createSEC_SPEC_2011_FRENCH_PRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2011_FRENCH_PRESSES), String.class, attribute);
	}

	public static final String SEC_SPEC_2012_POUROVER_COFFEE_MAKERS = "sec_spec_2012-PourOver_Coffee_Makers";
	@XmlElementDecl(name = SEC_SPEC_2012_POUROVER_COFFEE_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2012_POUROVER_COFFEE_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2012_POUROVER_COFFEE_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2013_SINGLE_SERVE_PODS = "sec_spec_2013-Single_Serve_Pods";
	@XmlElementDecl(name = SEC_SPEC_2013_SINGLE_SERVE_PODS)
	public JAXBElement<String> createSEC_SPEC_2013_SINGLE_SERVE_PODS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2013_SINGLE_SERVE_PODS), String.class, attribute);
	}

	public static final String SEC_SPEC_2015_KETTLES = "sec_spec_2015-Kettles";
	@XmlElementDecl(name = SEC_SPEC_2015_KETTLES)
	public JAXBElement<String> createSEC_SPEC_2015_KETTLES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2015_KETTLES), String.class, attribute);
	}

	public static final String SEC_SPEC_2016_TEA_POTS = "sec_spec_2016-Tea_Pots";
	@XmlElementDecl(name = SEC_SPEC_2016_TEA_POTS)
	public JAXBElement<String> createSEC_SPEC_2016_TEA_POTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2016_TEA_POTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2017_COOKWARE = "sec_spec_2017-Cookware";
	@XmlElementDecl(name = SEC_SPEC_2017_COOKWARE)
	public JAXBElement<String> createSEC_SPEC_2017_COOKWARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2017_COOKWARE), String.class, attribute);
	}

	public static final String SEC_SPEC_2018_COOKWARE_SETS = "sec_spec_2018-Cookware_Sets";
	@XmlElementDecl(name = SEC_SPEC_2018_COOKWARE_SETS)
	public JAXBElement<String> createSEC_SPEC_2018_COOKWARE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2018_COOKWARE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2027_KITCHEN_SCISSORS = "sec_spec_2027-Kitchen_Scissors";
	@XmlElementDecl(name = SEC_SPEC_2027_KITCHEN_SCISSORS)
	public JAXBElement<String> createSEC_SPEC_2027_KITCHEN_SCISSORS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2027_KITCHEN_SCISSORS), String.class, attribute);
	}

	public static final String SEC_SPEC_2030_HANDHELD_KNIFE_SHARPENERS = "sec_spec_2030-Handheld_Knife_Sharpeners";
	@XmlElementDecl(name = SEC_SPEC_2030_HANDHELD_KNIFE_SHARPENERS)
	public JAXBElement<String> createSEC_SPEC_2030_HANDHELD_KNIFE_SHARPENERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2030_HANDHELD_KNIFE_SHARPENERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2031_KNIFE_BLOCKS = "sec_spec_2031-Knife_Blocks";
	@XmlElementDecl(name = SEC_SPEC_2031_KNIFE_BLOCKS)
	public JAXBElement<String> createSEC_SPEC_2031_KNIFE_BLOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2031_KNIFE_BLOCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_2032_KNIVES = "sec_spec_2032-Knives";
	@XmlElementDecl(name = SEC_SPEC_2032_KNIVES)
	public JAXBElement<String> createSEC_SPEC_2032_KNIVES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2032_KNIVES), String.class, attribute);
	}

	public static final String SEC_SPEC_2036_KNIFE_SETS = "sec_spec_2036-Knife_Sets";
	@XmlElementDecl(name = SEC_SPEC_2036_KNIFE_SETS)
	public JAXBElement<String> createSEC_SPEC_2036_KNIFE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2036_KNIFE_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2039_SPECIALTY_KNIVES = "sec_spec_2039-Specialty_Knives";
	@XmlElementDecl(name = SEC_SPEC_2039_SPECIALTY_KNIVES)
	public JAXBElement<String> createSEC_SPEC_2039_SPECIALTY_KNIVES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2039_SPECIALTY_KNIVES), String.class, attribute);
	}

	public static final String SEC_SPEC_2042_FOOD_STORAGE = "sec_spec_2042-Food_Storage";
	@XmlElementDecl(name = SEC_SPEC_2042_FOOD_STORAGE)
	public JAXBElement<String> createSEC_SPEC_2042_FOOD_STORAGE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2042_FOOD_STORAGE), String.class, attribute);
	}

	public static final String SEC_SPEC_2043_CANISTERS_AND_CROCKS = "sec_spec_2043-Canisters_and_Crocks";
	@XmlElementDecl(name = SEC_SPEC_2043_CANISTERS_AND_CROCKS)
	public JAXBElement<String> createSEC_SPEC_2043_CANISTERS_AND_CROCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2043_CANISTERS_AND_CROCKS), String.class, attribute);
	}

	public static final String SEC_SPEC_2044_CONDIMENT_AND_UTENSIL_CADDIES = "sec_spec_2044-Condiment_and_Utensil_Caddies";
	@XmlElementDecl(name = SEC_SPEC_2044_CONDIMENT_AND_UTENSIL_CADDIES)
	public JAXBElement<String> createSEC_SPEC_2044_CONDIMENT_AND_UTENSIL_CADDIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2044_CONDIMENT_AND_UTENSIL_CADDIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2046_COOLERS = "sec_spec_2046-Coolers";
	@XmlElementDecl(name = SEC_SPEC_2046_COOLERS)
	public JAXBElement<String> createSEC_SPEC_2046_COOLERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2046_COOLERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2048_FOOD_STORAGE_CONTAINERS = "sec_spec_2048-Food_Storage_Containers";
	@XmlElementDecl(name = SEC_SPEC_2048_FOOD_STORAGE_CONTAINERS)
	public JAXBElement<String> createSEC_SPEC_2048_FOOD_STORAGE_CONTAINERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2048_FOOD_STORAGE_CONTAINERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2049_LUNCH_BAGS_AND_TOTES = "sec_spec_2049-Lunch_Bags_and_Totes";
	@XmlElementDecl(name = SEC_SPEC_2049_LUNCH_BAGS_AND_TOTES)
	public JAXBElement<String> createSEC_SPEC_2049_LUNCH_BAGS_AND_TOTES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2049_LUNCH_BAGS_AND_TOTES), String.class, attribute);
	}

	public static final String SEC_SPEC_2050_PICNIC_BASKETS = "sec_spec_2050-Picnic_Baskets";
	@XmlElementDecl(name = SEC_SPEC_2050_PICNIC_BASKETS)
	public JAXBElement<String> createSEC_SPEC_2050_PICNIC_BASKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2050_PICNIC_BASKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2051_STORAGE_CONTAINER_SETS = "sec_spec_2051-Storage_Container_Sets";
	@XmlElementDecl(name = SEC_SPEC_2051_STORAGE_CONTAINER_SETS)
	public JAXBElement<String> createSEC_SPEC_2051_STORAGE_CONTAINER_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2051_STORAGE_CONTAINER_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2053_GRILL_TOOLS_AND_ACCESSORIES = "sec_spec_2053-Grill_Tools_and_Accessories";
	@XmlElementDecl(name = SEC_SPEC_2053_GRILL_TOOLS_AND_ACCESSORIES)
	public JAXBElement<String> createSEC_SPEC_2053_GRILL_TOOLS_AND_ACCESSORIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2053_GRILL_TOOLS_AND_ACCESSORIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2055_KITCHEN_APPLIANCES = "sec_spec_2055-Kitchen_Appliances";
	@XmlElementDecl(name = SEC_SPEC_2055_KITCHEN_APPLIANCES)
	public JAXBElement<String> createSEC_SPEC_2055_KITCHEN_APPLIANCES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2055_KITCHEN_APPLIANCES), String.class, attribute);
	}

	public static final String SEC_SPEC_2057_COFFEE_GRINDERS = "sec_spec_2057-Coffee_Grinders";
	@XmlElementDecl(name = SEC_SPEC_2057_COFFEE_GRINDERS)
	public JAXBElement<String> createSEC_SPEC_2057_COFFEE_GRINDERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2057_COFFEE_GRINDERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2058_DRIP_COFFEE_MAKERS = "sec_spec_2058-Drip_Coffee_Makers";
	@XmlElementDecl(name = SEC_SPEC_2058_DRIP_COFFEE_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2058_DRIP_COFFEE_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2058_DRIP_COFFEE_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2059_ESPRESSO_MACHINES = "sec_spec_2059-Espresso_Machines";
	@XmlElementDecl(name = SEC_SPEC_2059_ESPRESSO_MACHINES)
	public JAXBElement<String> createSEC_SPEC_2059_ESPRESSO_MACHINES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2059_ESPRESSO_MACHINES), String.class, attribute);
	}

	public static final String SEC_SPEC_2060_ICED_TEA_MAKERS = "sec_spec_2060-Iced_Tea_Makers";
	@XmlElementDecl(name = SEC_SPEC_2060_ICED_TEA_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2060_ICED_TEA_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2060_ICED_TEA_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2061_SINGLE_SERVE_BREWERS = "sec_spec_2061-Single_Serve_Brewers";
	@XmlElementDecl(name = SEC_SPEC_2061_SINGLE_SERVE_BREWERS)
	public JAXBElement<String> createSEC_SPEC_2061_SINGLE_SERVE_BREWERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2061_SINGLE_SERVE_BREWERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2064_BURNERS_AND_HOT_PLATES = "sec_spec_2064-Burners_and_Hot_Plates";
	@XmlElementDecl(name = SEC_SPEC_2064_BURNERS_AND_HOT_PLATES)
	public JAXBElement<String> createSEC_SPEC_2064_BURNERS_AND_HOT_PLATES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2064_BURNERS_AND_HOT_PLATES), String.class, attribute);
	}

	public static final String SEC_SPEC_2065_DEEP_FRYERS = "sec_spec_2065-Deep_Fryers";
	@XmlElementDecl(name = SEC_SPEC_2065_DEEP_FRYERS)
	public JAXBElement<String> createSEC_SPEC_2065_DEEP_FRYERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2065_DEEP_FRYERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2066_ELECTRIC_GRIDDLES_AND_GRILLS = "sec_spec_2066-Electric_Griddles_and_Grills";
	@XmlElementDecl(name = SEC_SPEC_2066_ELECTRIC_GRIDDLES_AND_GRILLS)
	public JAXBElement<String> createSEC_SPEC_2066_ELECTRIC_GRIDDLES_AND_GRILLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2066_ELECTRIC_GRIDDLES_AND_GRILLS), String.class, attribute);
	}

	public static final String SEC_SPEC_2067_ELECTRIC_PRESSURE_COOKERS = "sec_spec_2067-Electric_Pressure_Cookers";
	@XmlElementDecl(name = SEC_SPEC_2067_ELECTRIC_PRESSURE_COOKERS)
	public JAXBElement<String> createSEC_SPEC_2067_ELECTRIC_PRESSURE_COOKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2067_ELECTRIC_PRESSURE_COOKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2068_ELECTRIC_SKILLETS = "sec_spec_2068-Electric_Skillets";
	@XmlElementDecl(name = SEC_SPEC_2068_ELECTRIC_SKILLETS)
	public JAXBElement<String> createSEC_SPEC_2068_ELECTRIC_SKILLETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2068_ELECTRIC_SKILLETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2069_SLOW_COOKERS = "sec_spec_2069-Slow_Cookers";
	@XmlElementDecl(name = SEC_SPEC_2069_SLOW_COOKERS)
	public JAXBElement<String> createSEC_SPEC_2069_SLOW_COOKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2069_SLOW_COOKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2070_TOASTERS_AND_OVENS = "sec_spec_2070-Toasters_and_Ovens";
	@XmlElementDecl(name = SEC_SPEC_2070_TOASTERS_AND_OVENS)
	public JAXBElement<String> createSEC_SPEC_2070_TOASTERS_AND_OVENS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2070_TOASTERS_AND_OVENS), String.class, attribute);
	}

	public static final String SEC_SPEC_2072_BLENDERS = "sec_spec_2072-Blenders";
	@XmlElementDecl(name = SEC_SPEC_2072_BLENDERS)
	public JAXBElement<String> createSEC_SPEC_2072_BLENDERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2072_BLENDERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2073_ELECTRIC_CAN_OPENERS = "sec_spec_2073-Electric_Can_Openers";
	@XmlElementDecl(name = SEC_SPEC_2073_ELECTRIC_CAN_OPENERS)
	public JAXBElement<String> createSEC_SPEC_2073_ELECTRIC_CAN_OPENERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2073_ELECTRIC_CAN_OPENERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2074_ELECTRIC_FOOD_SLICERS = "sec_spec_2074-Electric_Food_Slicers";
	@XmlElementDecl(name = SEC_SPEC_2074_ELECTRIC_FOOD_SLICERS)
	public JAXBElement<String> createSEC_SPEC_2074_ELECTRIC_FOOD_SLICERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2074_ELECTRIC_FOOD_SLICERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2075_ELECTRIC_KNIFE_SHARPENERS = "sec_spec_2075-Electric_Knife_Sharpeners";
	@XmlElementDecl(name = SEC_SPEC_2075_ELECTRIC_KNIFE_SHARPENERS)
	public JAXBElement<String> createSEC_SPEC_2075_ELECTRIC_KNIFE_SHARPENERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2075_ELECTRIC_KNIFE_SHARPENERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2076_FOOD_PROCESSORS_AND_CHOPPERS = "sec_spec_2076-Food_Processors_and_Choppers";
	@XmlElementDecl(name = SEC_SPEC_2076_FOOD_PROCESSORS_AND_CHOPPERS)
	public JAXBElement<String> createSEC_SPEC_2076_FOOD_PROCESSORS_AND_CHOPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2076_FOOD_PROCESSORS_AND_CHOPPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2077_HAND_MIXERS_AND_IMMERSION_BLENDERS = "sec_spec_2077-Hand_Mixers_and_Immersion_Blenders";
	@XmlElementDecl(name = SEC_SPEC_2077_HAND_MIXERS_AND_IMMERSION_BLENDERS)
	public JAXBElement<String> createSEC_SPEC_2077_HAND_MIXERS_AND_IMMERSION_BLENDERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2077_HAND_MIXERS_AND_IMMERSION_BLENDERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2078_JUICERS = "sec_spec_2078-Juicers";
	@XmlElementDecl(name = SEC_SPEC_2078_JUICERS)
	public JAXBElement<String> createSEC_SPEC_2078_JUICERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2078_JUICERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2080_STAND_MIXERS = "sec_spec_2080-Stand_Mixers";
	@XmlElementDecl(name = SEC_SPEC_2080_STAND_MIXERS)
	public JAXBElement<String> createSEC_SPEC_2080_STAND_MIXERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2080_STAND_MIXERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2082_BEVERAGE_SERVING_APPLIANCES = "sec_spec_2082-Beverage_Serving_Appliances";
	@XmlElementDecl(name = SEC_SPEC_2082_BEVERAGE_SERVING_APPLIANCES)
	public JAXBElement<String> createSEC_SPEC_2082_BEVERAGE_SERVING_APPLIANCES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2082_BEVERAGE_SERVING_APPLIANCES), String.class, attribute);
	}

	public static final String SEC_SPEC_2083_BUFFET_STATIONS_AND_WARMERS = "sec_spec_2083-Buffet_Stations_and_Warmers";
	@XmlElementDecl(name = SEC_SPEC_2083_BUFFET_STATIONS_AND_WARMERS)
	public JAXBElement<String> createSEC_SPEC_2083_BUFFET_STATIONS_AND_WARMERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2083_BUFFET_STATIONS_AND_WARMERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2085_ELECTRIC_KNIVES = "sec_spec_2085-Electric_Knives";
	@XmlElementDecl(name = SEC_SPEC_2085_ELECTRIC_KNIVES)
	public JAXBElement<String> createSEC_SPEC_2085_ELECTRIC_KNIVES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2085_ELECTRIC_KNIVES), String.class, attribute);
	}

	public static final String SEC_SPEC_2088_FOOD_DEHYDRATORS = "sec_spec_2088-Food_Dehydrators";
	@XmlElementDecl(name = SEC_SPEC_2088_FOOD_DEHYDRATORS)
	public JAXBElement<String> createSEC_SPEC_2088_FOOD_DEHYDRATORS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2088_FOOD_DEHYDRATORS), String.class, attribute);
	}

	public static final String SEC_SPEC_2090_ICE_CREAM_AND_DESSERT_MAKERS = "sec_spec_2090-Ice_Cream_and_Dessert_Makers";
	@XmlElementDecl(name = SEC_SPEC_2090_ICE_CREAM_AND_DESSERT_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2090_ICE_CREAM_AND_DESSERT_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2090_ICE_CREAM_AND_DESSERT_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2091_POPCORN_MAKERS = "sec_spec_2091-Popcorn_Makers";
	@XmlElementDecl(name = SEC_SPEC_2091_POPCORN_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2091_POPCORN_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2091_POPCORN_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2092_SPECIALTY_FOOD_MAKERS = "sec_spec_2092-Specialty_Food_Makers";
	@XmlElementDecl(name = SEC_SPEC_2092_SPECIALTY_FOOD_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2092_SPECIALTY_FOOD_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2092_SPECIALTY_FOOD_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2093_STEAMERS_AND_RICE_COOKERS = "sec_spec_2093-Steamers_and_Rice_Cookers";
	@XmlElementDecl(name = SEC_SPEC_2093_STEAMERS_AND_RICE_COOKERS)
	public JAXBElement<String> createSEC_SPEC_2093_STEAMERS_AND_RICE_COOKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2093_STEAMERS_AND_RICE_COOKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2094_VACUUM_SEALERS_AND_ACCESSORIES = "sec_spec_2094-Vacuum_Sealers_and_Accessories";
	@XmlElementDecl(name = SEC_SPEC_2094_VACUUM_SEALERS_AND_ACCESSORIES)
	public JAXBElement<String> createSEC_SPEC_2094_VACUUM_SEALERS_AND_ACCESSORIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2094_VACUUM_SEALERS_AND_ACCESSORIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2095_WAFFLE_MAKERS = "sec_spec_2095-Waffle_Makers";
	@XmlElementDecl(name = SEC_SPEC_2095_WAFFLE_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2095_WAFFLE_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2095_WAFFLE_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2096_YOGURT_MAKERS = "sec_spec_2096-Yogurt_Makers";
	@XmlElementDecl(name = SEC_SPEC_2096_YOGURT_MAKERS)
	public JAXBElement<String> createSEC_SPEC_2096_YOGURT_MAKERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2096_YOGURT_MAKERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2098_KITCHEN_LINEN_SETS = "sec_spec_2098-Kitchen_Linen_Sets";
	@XmlElementDecl(name = SEC_SPEC_2098_KITCHEN_LINEN_SETS)
	public JAXBElement<String> createSEC_SPEC_2098_KITCHEN_LINEN_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2098_KITCHEN_LINEN_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2100_POT_HOLDERS_AND_OVEN_MITTS = "sec_spec_2100-Pot_Holders_and_Oven_Mitts";
	@XmlElementDecl(name = SEC_SPEC_2100_POT_HOLDERS_AND_OVEN_MITTS)
	public JAXBElement<String> createSEC_SPEC_2100_POT_HOLDERS_AND_OVEN_MITTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2100_POT_HOLDERS_AND_OVEN_MITTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2102_CABINET_ORGANIZERS = "sec_spec_2102-Cabinet_Organizers";
	@XmlElementDecl(name = SEC_SPEC_2102_CABINET_ORGANIZERS)
	public JAXBElement<String> createSEC_SPEC_2102_CABINET_ORGANIZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2102_CABINET_ORGANIZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2104_DRAWER_ORGANIZERS = "sec_spec_2104-Drawer_Organizers";
	@XmlElementDecl(name = SEC_SPEC_2104_DRAWER_ORGANIZERS)
	public JAXBElement<String> createSEC_SPEC_2104_DRAWER_ORGANIZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2104_DRAWER_ORGANIZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2105_LAZY_SUSANS = "sec_spec_2105-Lazy_Susans";
	@XmlElementDecl(name = SEC_SPEC_2105_LAZY_SUSANS)
	public JAXBElement<String> createSEC_SPEC_2105_LAZY_SUSANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2105_LAZY_SUSANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2106_PAPER_TOWEL_HOLDERS = "sec_spec_2106-Paper_Towel_Holders";
	@XmlElementDecl(name = SEC_SPEC_2106_PAPER_TOWEL_HOLDERS)
	public JAXBElement<String> createSEC_SPEC_2106_PAPER_TOWEL_HOLDERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2106_PAPER_TOWEL_HOLDERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2108_RECIPE_ORGANIZERS = "sec_spec_2108-Recipe_Organizers";
	@XmlElementDecl(name = SEC_SPEC_2108_RECIPE_ORGANIZERS)
	public JAXBElement<String> createSEC_SPEC_2108_RECIPE_ORGANIZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2108_RECIPE_ORGANIZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2109_SINK_ORGANIZERS = "sec_spec_2109-Sink_Organizers";
	@XmlElementDecl(name = SEC_SPEC_2109_SINK_ORGANIZERS)
	public JAXBElement<String> createSEC_SPEC_2109_SINK_ORGANIZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2109_SINK_ORGANIZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2110_SPICE_RACKS = "sec_spec_2110-Spice_Racks";
	@XmlElementDecl(name = SEC_SPEC_2110_SPICE_RACKS)
	public JAXBElement<String> createSEC_SPEC_2110_SPICE_RACKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2110_SPICE_RACKS), String.class, attribute);
	}

	public static final String SEC_SPEC_2111_KITCHEN_TOOLS_AND_UTENSILS = "sec_spec_2111-Kitchen_Tools_and_Utensils";
	@XmlElementDecl(name = SEC_SPEC_2111_KITCHEN_TOOLS_AND_UTENSILS)
	public JAXBElement<String> createSEC_SPEC_2111_KITCHEN_TOOLS_AND_UTENSILS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2111_KITCHEN_TOOLS_AND_UTENSILS), String.class, attribute);
	}

	public static final String SEC_SPEC_2112_BAKING_AND_DECORATING_TOOLS = "sec_spec_2112-Baking_and_Decorating_Tools";
	@XmlElementDecl(name = SEC_SPEC_2112_BAKING_AND_DECORATING_TOOLS)
	public JAXBElement<String> createSEC_SPEC_2112_BAKING_AND_DECORATING_TOOLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2112_BAKING_AND_DECORATING_TOOLS), String.class, attribute);
	}

	public static final String SEC_SPEC_2114_CHOPPING_AND_SLICING_TOOLS = "sec_spec_2114-Chopping_and_Slicing_Tools";
	@XmlElementDecl(name = SEC_SPEC_2114_CHOPPING_AND_SLICING_TOOLS)
	public JAXBElement<String> createSEC_SPEC_2114_CHOPPING_AND_SLICING_TOOLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2114_CHOPPING_AND_SLICING_TOOLS), String.class, attribute);
	}

	public static final String SEC_SPEC_2115_COLANDERS_AND_STRAINERS = "sec_spec_2115-Colanders_and_Strainers";
	@XmlElementDecl(name = SEC_SPEC_2115_COLANDERS_AND_STRAINERS)
	public JAXBElement<String> createSEC_SPEC_2115_COLANDERS_AND_STRAINERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2115_COLANDERS_AND_STRAINERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2116_COOKING_UTENSILS = "sec_spec_2116-Cooking_Utensils";
	@XmlElementDecl(name = SEC_SPEC_2116_COOKING_UTENSILS)
	public JAXBElement<String> createSEC_SPEC_2116_COOKING_UTENSILS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2116_COOKING_UTENSILS), String.class, attribute);
	}

	public static final String SEC_SPEC_2117_FOOD_SCALES = "sec_spec_2117-Food_Scales";
	@XmlElementDecl(name = SEC_SPEC_2117_FOOD_SCALES)
	public JAXBElement<String> createSEC_SPEC_2117_FOOD_SCALES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2117_FOOD_SCALES), String.class, attribute);
	}

	public static final String SEC_SPEC_2118_GRATERS = "sec_spec_2118-Graters";
	@XmlElementDecl(name = SEC_SPEC_2118_GRATERS)
	public JAXBElement<String> createSEC_SPEC_2118_GRATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2118_GRATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2119_MEASURING_CUPS = "sec_spec_2119-Measuring_Cups";
	@XmlElementDecl(name = SEC_SPEC_2119_MEASURING_CUPS)
	public JAXBElement<String> createSEC_SPEC_2119_MEASURING_CUPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2119_MEASURING_CUPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2120_MEASURING_SPOONS_AND_SCOOPS = "sec_spec_2120-Measuring_Spoons_and_Scoops";
	@XmlElementDecl(name = SEC_SPEC_2120_MEASURING_SPOONS_AND_SCOOPS)
	public JAXBElement<String> createSEC_SPEC_2120_MEASURING_SPOONS_AND_SCOOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2120_MEASURING_SPOONS_AND_SCOOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2121_MIXING_AND_PREP_BOWLS = "sec_spec_2121-Mixing_and_Prep_Bowls";
	@XmlElementDecl(name = SEC_SPEC_2121_MIXING_AND_PREP_BOWLS)
	public JAXBElement<String> createSEC_SPEC_2121_MIXING_AND_PREP_BOWLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2121_MIXING_AND_PREP_BOWLS), String.class, attribute);
	}

	public static final String SEC_SPEC_2122_PEELERS = "sec_spec_2122-Peelers";
	@XmlElementDecl(name = SEC_SPEC_2122_PEELERS)
	public JAXBElement<String> createSEC_SPEC_2122_PEELERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2122_PEELERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2123_PIZZA_TOOLS = "sec_spec_2123-Pizza_Tools";
	@XmlElementDecl(name = SEC_SPEC_2123_PIZZA_TOOLS)
	public JAXBElement<String> createSEC_SPEC_2123_PIZZA_TOOLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2123_PIZZA_TOOLS), String.class, attribute);
	}

	public static final String SEC_SPEC_2124_SCRUBS_AND_BRUSHES = "sec_spec_2124-Scrubs_and_Brushes";
	@XmlElementDecl(name = SEC_SPEC_2124_SCRUBS_AND_BRUSHES)
	public JAXBElement<String> createSEC_SPEC_2124_SCRUBS_AND_BRUSHES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2124_SCRUBS_AND_BRUSHES), String.class, attribute);
	}

	public static final String SEC_SPEC_2125_SEASONING_AND_SPICE_TOOLS = "sec_spec_2125-Seasoning_and_Spice_Tools";
	@XmlElementDecl(name = SEC_SPEC_2125_SEASONING_AND_SPICE_TOOLS)
	public JAXBElement<String> createSEC_SPEC_2125_SEASONING_AND_SPICE_TOOLS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2125_SEASONING_AND_SPICE_TOOLS), String.class, attribute);
	}

	public static final String SEC_SPEC_2126_SPECIALTY_KITCHEN_GADGETS = "sec_spec_2126-Specialty_Kitchen_Gadgets";
	@XmlElementDecl(name = SEC_SPEC_2126_SPECIALTY_KITCHEN_GADGETS)
	public JAXBElement<String> createSEC_SPEC_2126_SPECIALTY_KITCHEN_GADGETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2126_SPECIALTY_KITCHEN_GADGETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2127_THERMOMETERS_AND_TIMERS = "sec_spec_2127-Thermometers_and_Timers";
	@XmlElementDecl(name = SEC_SPEC_2127_THERMOMETERS_AND_TIMERS)
	public JAXBElement<String> createSEC_SPEC_2127_THERMOMETERS_AND_TIMERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2127_THERMOMETERS_AND_TIMERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2128_TOOL_AND_UTENSIL_SETS = "sec_spec_2128-Tool_and_Utensil_Sets";
	@XmlElementDecl(name = SEC_SPEC_2128_TOOL_AND_UTENSIL_SETS)
	public JAXBElement<String> createSEC_SPEC_2128_TOOL_AND_UTENSIL_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2128_TOOL_AND_UTENSIL_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2129_PERSONAL_CARE = "sec_spec_2129-Personal_Care";
	@XmlElementDecl(name = SEC_SPEC_2129_PERSONAL_CARE)
	public JAXBElement<String> createSEC_SPEC_2129_PERSONAL_CARE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2129_PERSONAL_CARE), String.class, attribute);
	}

	public static final String SEC_SPEC_2130_BATH_SCALES = "sec_spec_2130-Bath_Scales";
	@XmlElementDecl(name = SEC_SPEC_2130_BATH_SCALES)
	public JAXBElement<String> createSEC_SPEC_2130_BATH_SCALES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2130_BATH_SCALES), String.class, attribute);
	}

	public static final String SEC_SPEC_2132_TOOTHBRUSH_HEADS = "sec_spec_2132-Toothbrush_Heads";
	@XmlElementDecl(name = SEC_SPEC_2132_TOOTHBRUSH_HEADS)
	public JAXBElement<String> createSEC_SPEC_2132_TOOTHBRUSH_HEADS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2132_TOOTHBRUSH_HEADS), String.class, attribute);
	}

	public static final String SEC_SPEC_2138_SHAVERS_AND_TRIMMERS = "sec_spec_2138-Shavers_and_Trimmers";
	@XmlElementDecl(name = SEC_SPEC_2138_SHAVERS_AND_TRIMMERS)
	public JAXBElement<String> createSEC_SPEC_2138_SHAVERS_AND_TRIMMERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2138_SHAVERS_AND_TRIMMERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2139_ELECTRIC_RAZORS = "sec_spec_2139-Electric_Razors";
	@XmlElementDecl(name = SEC_SPEC_2139_ELECTRIC_RAZORS)
	public JAXBElement<String> createSEC_SPEC_2139_ELECTRIC_RAZORS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2139_ELECTRIC_RAZORS), String.class, attribute);
	}

	public static final String SEC_SPEC_2141_HAIR_TRIMMERS = "sec_spec_2141-Hair_Trimmers";
	@XmlElementDecl(name = SEC_SPEC_2141_HAIR_TRIMMERS)
	public JAXBElement<String> createSEC_SPEC_2141_HAIR_TRIMMERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2141_HAIR_TRIMMERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2145_RUGS = "sec_spec_2145-Rugs";
	@XmlElementDecl(name = SEC_SPEC_2145_RUGS)
	public JAXBElement<String> createSEC_SPEC_2145_RUGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2145_RUGS), String.class, attribute);
	}

	public static final String SEC_SPEC_2147_KITCHEN_MATS = "sec_spec_2147-Kitchen_Mats";
	@XmlElementDecl(name = SEC_SPEC_2147_KITCHEN_MATS)
	public JAXBElement<String> createSEC_SPEC_2147_KITCHEN_MATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2147_KITCHEN_MATS), String.class, attribute);
	}

	public static final String SEC_SPEC_2149_WINDOW_TREATMENTS = "sec_spec_2149-Window_Treatments";
	@XmlElementDecl(name = SEC_SPEC_2149_WINDOW_TREATMENTS)
	public JAXBElement<String> createSEC_SPEC_2149_WINDOW_TREATMENTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2149_WINDOW_TREATMENTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2150_CURTAIN_RODS_AND_CLIPS = "sec_spec_2150-Curtain_Rods_and_Clips";
	@XmlElementDecl(name = SEC_SPEC_2150_CURTAIN_RODS_AND_CLIPS)
	public JAXBElement<String> createSEC_SPEC_2150_CURTAIN_RODS_AND_CLIPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2150_CURTAIN_RODS_AND_CLIPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2151_CURTAINS_AND_DRAPES = "sec_spec_2151-Curtains_and_Drapes";
	@XmlElementDecl(name = SEC_SPEC_2151_CURTAINS_AND_DRAPES)
	public JAXBElement<String> createSEC_SPEC_2151_CURTAINS_AND_DRAPES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2151_CURTAINS_AND_DRAPES), String.class, attribute);
	}

	public static final String SEC_SPEC_2157_JUNIORS = "sec_spec_2157-Juniors";
	@XmlElementDecl(name = SEC_SPEC_2157_JUNIORS)
	public JAXBElement<String> createSEC_SPEC_2157_JUNIORS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2157_JUNIORS), String.class, attribute);
	}

	public static final String SEC_SPEC_2158_COATS = "sec_spec_2158-Coats";
	@XmlElementDecl(name = SEC_SPEC_2158_COATS)
	public JAXBElement<String> createSEC_SPEC_2158_COATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2158_COATS), String.class, attribute);
	}

	public static final String SEC_SPEC_2167_DRESSES = "sec_spec_2167-Dresses";
	@XmlElementDecl(name = SEC_SPEC_2167_DRESSES)
	public JAXBElement<String> createSEC_SPEC_2167_DRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2167_DRESSES), String.class, attribute);
	}

	public static final String SEC_SPEC_2168_JACKETS_AND_BLAZERS = "sec_spec_2168-Jackets_and_Blazers";
	@XmlElementDecl(name = SEC_SPEC_2168_JACKETS_AND_BLAZERS)
	public JAXBElement<String> createSEC_SPEC_2168_JACKETS_AND_BLAZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2168_JACKETS_AND_BLAZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2169_JEANS = "sec_spec_2169-Jeans";
	@XmlElementDecl(name = SEC_SPEC_2169_JEANS)
	public JAXBElement<String> createSEC_SPEC_2169_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2169_JEANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2170_JUMPSUITS_AND_ROMPERS = "sec_spec_2170-Jumpsuits_and_Rompers";
	@XmlElementDecl(name = SEC_SPEC_2170_JUMPSUITS_AND_ROMPERS)
	public JAXBElement<String> createSEC_SPEC_2170_JUMPSUITS_AND_ROMPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2170_JUMPSUITS_AND_ROMPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2172_BRAS = "sec_spec_2172-Bras";
	@XmlElementDecl(name = SEC_SPEC_2172_BRAS)
	public JAXBElement<String> createSEC_SPEC_2172_BRAS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2172_BRAS), String.class, attribute);
	}

	public static final String SEC_SPEC_2182_PANTIES = "sec_spec_2182-Panties";
	@XmlElementDecl(name = SEC_SPEC_2182_PANTIES)
	public JAXBElement<String> createSEC_SPEC_2182_PANTIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2182_PANTIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2190_PANTS_AND_LEGGINGS = "sec_spec_2190-Pants_and_Leggings";
	@XmlElementDecl(name = SEC_SPEC_2190_PANTS_AND_LEGGINGS)
	public JAXBElement<String> createSEC_SPEC_2190_PANTS_AND_LEGGINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2190_PANTS_AND_LEGGINGS), String.class, attribute);
	}

	public static final String SEC_SPEC_2194_TROUSERS = "sec_spec_2194-Trousers";
	@XmlElementDecl(name = SEC_SPEC_2194_TROUSERS)
	public JAXBElement<String> createSEC_SPEC_2194_TROUSERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2194_TROUSERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2195_SHORTS = "sec_spec_2195-Shorts";
	@XmlElementDecl(name = SEC_SPEC_2195_SHORTS)
	public JAXBElement<String> createSEC_SPEC_2195_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2195_SHORTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2196_SKIRTS = "sec_spec_2196-Skirts";
	@XmlElementDecl(name = SEC_SPEC_2196_SKIRTS)
	public JAXBElement<String> createSEC_SPEC_2196_SKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2196_SKIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2197_SLEEPWEAR_AND_LOUNGEWEAR = "sec_spec_2197-Sleepwear_and_Loungewear";
	@XmlElementDecl(name = SEC_SPEC_2197_SLEEPWEAR_AND_LOUNGEWEAR)
	public JAXBElement<String> createSEC_SPEC_2197_SLEEPWEAR_AND_LOUNGEWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2197_SLEEPWEAR_AND_LOUNGEWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_2198_BOTTOMS = "sec_spec_2198-Bottoms";
	@XmlElementDecl(name = SEC_SPEC_2198_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_2198_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2198_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_2199_CHEMISES = "sec_spec_2199-Chemises";
	@XmlElementDecl(name = SEC_SPEC_2199_CHEMISES)
	public JAXBElement<String> createSEC_SPEC_2199_CHEMISES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2199_CHEMISES), String.class, attribute);
	}

	public static final String SEC_SPEC_2200_NIGHTGOWNS_AND_SLEEP_SHIRTS = "sec_spec_2200-Nightgowns_and_Sleep_Shirts";
	@XmlElementDecl(name = SEC_SPEC_2200_NIGHTGOWNS_AND_SLEEP_SHIRTS)
	public JAXBElement<String> createSEC_SPEC_2200_NIGHTGOWNS_AND_SLEEP_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2200_NIGHTGOWNS_AND_SLEEP_SHIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2201_PAJAMA_SETS = "sec_spec_2201-Pajama_Sets";
	@XmlElementDecl(name = SEC_SPEC_2201_PAJAMA_SETS)
	public JAXBElement<String> createSEC_SPEC_2201_PAJAMA_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2201_PAJAMA_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2202_ROBES = "sec_spec_2202-Robes";
	@XmlElementDecl(name = SEC_SPEC_2202_ROBES)
	public JAXBElement<String> createSEC_SPEC_2202_ROBES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2202_ROBES), String.class, attribute);
	}

	public static final String SEC_SPEC_2203_TOPS = "sec_spec_2203-Tops";
	@XmlElementDecl(name = SEC_SPEC_2203_TOPS)
	public JAXBElement<String> createSEC_SPEC_2203_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2203_TOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2204_SWEATERS_AND_HOODIES = "sec_spec_2204-Sweaters_and_Hoodies";
	@XmlElementDecl(name = SEC_SPEC_2204_SWEATERS_AND_HOODIES)
	public JAXBElement<String> createSEC_SPEC_2204_SWEATERS_AND_HOODIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2204_SWEATERS_AND_HOODIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2206_COVER_UPS = "sec_spec_2206-Cover_Ups";
	@XmlElementDecl(name = SEC_SPEC_2206_COVER_UPS)
	public JAXBElement<String> createSEC_SPEC_2206_COVER_UPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2206_COVER_UPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2207_ONE_PIECE = "sec_spec_2207-One_Piece";
	@XmlElementDecl(name = SEC_SPEC_2207_ONE_PIECE)
	public JAXBElement<String> createSEC_SPEC_2207_ONE_PIECE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2207_ONE_PIECE), String.class, attribute);
	}

	public static final String SEC_SPEC_2208_TWO_PIECE_BOTTOMS = "sec_spec_2208-Two_Piece_Bottoms";
	@XmlElementDecl(name = SEC_SPEC_2208_TWO_PIECE_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_2208_TWO_PIECE_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2208_TWO_PIECE_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_2209_TWO_PIECE_TOPS = "sec_spec_2209-Two_Piece_Tops";
	@XmlElementDecl(name = SEC_SPEC_2209_TWO_PIECE_TOPS)
	public JAXBElement<String> createSEC_SPEC_2209_TWO_PIECE_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2209_TWO_PIECE_TOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2210_TOPS = "sec_spec_2210-Tops";
	@XmlElementDecl(name = SEC_SPEC_2210_TOPS)
	public JAXBElement<String> createSEC_SPEC_2210_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2210_TOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2217_MEN = "sec_spec_2217-Men";
	@XmlElementDecl(name = SEC_SPEC_2217_MEN)
	public JAXBElement<String> createSEC_SPEC_2217_MEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2217_MEN), String.class, attribute);
	}

	public static final String SEC_SPEC_2218_COATS_AND_JACKETS = "sec_spec_2218-Coats_and_Jackets";
	@XmlElementDecl(name = SEC_SPEC_2218_COATS_AND_JACKETS)
	public JAXBElement<String> createSEC_SPEC_2218_COATS_AND_JACKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2218_COATS_AND_JACKETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2225_JEANS = "sec_spec_2225-Jeans";
	@XmlElementDecl(name = SEC_SPEC_2225_JEANS)
	public JAXBElement<String> createSEC_SPEC_2225_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2225_JEANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2226_PANTS = "sec_spec_2226-Pants";
	@XmlElementDecl(name = SEC_SPEC_2226_PANTS)
	public JAXBElement<String> createSEC_SPEC_2226_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2226_PANTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2229_DRESS = "sec_spec_2229-Dress";
	@XmlElementDecl(name = SEC_SPEC_2229_DRESS)
	public JAXBElement<String> createSEC_SPEC_2229_DRESS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2229_DRESS), String.class, attribute);
	}

	public static final String SEC_SPEC_2230_SHIRTS = "sec_spec_2230-Shirts";
	@XmlElementDecl(name = SEC_SPEC_2230_SHIRTS)
	public JAXBElement<String> createSEC_SPEC_2230_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2230_SHIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2234_DRESS_SHIRTS = "sec_spec_2234-Dress_Shirts";
	@XmlElementDecl(name = SEC_SPEC_2234_DRESS_SHIRTS)
	public JAXBElement<String> createSEC_SPEC_2234_DRESS_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2234_DRESS_SHIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2236_T_SHIRTS = "sec_spec_2236-T_Shirts";
	@XmlElementDecl(name = SEC_SPEC_2236_T_SHIRTS)
	public JAXBElement<String> createSEC_SPEC_2236_T_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2236_T_SHIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2237_SHORTS = "sec_spec_2237-Shorts";
	@XmlElementDecl(name = SEC_SPEC_2237_SHORTS)
	public JAXBElement<String> createSEC_SPEC_2237_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2237_SHORTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2242_SUITS_AND_SPORTCOATS = "sec_spec_2242-Suits_and_Sportcoats";
	@XmlElementDecl(name = SEC_SPEC_2242_SUITS_AND_SPORTCOATS)
	public JAXBElement<String> createSEC_SPEC_2242_SUITS_AND_SPORTCOATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2242_SUITS_AND_SPORTCOATS), String.class, attribute);
	}

	public static final String SEC_SPEC_2243_BLAZERS_AND_SPORTCOATS = "sec_spec_2243-Blazers_and_Sportcoats";
	@XmlElementDecl(name = SEC_SPEC_2243_BLAZERS_AND_SPORTCOATS)
	public JAXBElement<String> createSEC_SPEC_2243_BLAZERS_AND_SPORTCOATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2243_BLAZERS_AND_SPORTCOATS), String.class, attribute);
	}

	public static final String SEC_SPEC_2244_FORMAL_SUITS = "sec_spec_2244-Formal_Suits";
	@XmlElementDecl(name = SEC_SPEC_2244_FORMAL_SUITS)
	public JAXBElement<String> createSEC_SPEC_2244_FORMAL_SUITS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2244_FORMAL_SUITS), String.class, attribute);
	}

	public static final String SEC_SPEC_2246_SEPARATE_SUIT_PANTS = "sec_spec_2246-Separate_Suit_Pants";
	@XmlElementDecl(name = SEC_SPEC_2246_SEPARATE_SUIT_PANTS)
	public JAXBElement<String> createSEC_SPEC_2246_SEPARATE_SUIT_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2246_SEPARATE_SUIT_PANTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2247_SUITS = "sec_spec_2247-Suits";
	@XmlElementDecl(name = SEC_SPEC_2247_SUITS)
	public JAXBElement<String> createSEC_SPEC_2247_SUITS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2247_SUITS), String.class, attribute);
	}

	public static final String SEC_SPEC_2249_SWEATERS = "sec_spec_2249-Sweaters";
	@XmlElementDecl(name = SEC_SPEC_2249_SWEATERS)
	public JAXBElement<String> createSEC_SPEC_2249_SWEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2249_SWEATERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2253_SWEATSHIRTS_AND_HOODIES = "sec_spec_2253-Sweatshirts_and_Hoodies";
	@XmlElementDecl(name = SEC_SPEC_2253_SWEATSHIRTS_AND_HOODIES)
	public JAXBElement<String> createSEC_SPEC_2253_SWEATSHIRTS_AND_HOODIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2253_SWEATSHIRTS_AND_HOODIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2255_BOARD_SHORTS = "sec_spec_2255-Board_Shorts";
	@XmlElementDecl(name = SEC_SPEC_2255_BOARD_SHORTS)
	public JAXBElement<String> createSEC_SPEC_2255_BOARD_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2255_BOARD_SHORTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2256_SWIM_TEES = "sec_spec_2256-Swim_Tees";
	@XmlElementDecl(name = SEC_SPEC_2256_SWIM_TEES)
	public JAXBElement<String> createSEC_SPEC_2256_SWIM_TEES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2256_SWIM_TEES), String.class, attribute);
	}

	public static final String SEC_SPEC_2257_SWIM_TRUNKS = "sec_spec_2257-Swim_Trunks";
	@XmlElementDecl(name = SEC_SPEC_2257_SWIM_TRUNKS)
	public JAXBElement<String> createSEC_SPEC_2257_SWIM_TRUNKS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2257_SWIM_TRUNKS), String.class, attribute);
	}

	public static final String SEC_SPEC_2258_UNDERWEAR = "sec_spec_2258-Underwear";
	@XmlElementDecl(name = SEC_SPEC_2258_UNDERWEAR)
	public JAXBElement<String> createSEC_SPEC_2258_UNDERWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2258_UNDERWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_2263_UNDERSHIRTS = "sec_spec_2263-Undershirts";
	@XmlElementDecl(name = SEC_SPEC_2263_UNDERSHIRTS)
	public JAXBElement<String> createSEC_SPEC_2263_UNDERSHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2263_UNDERSHIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2264_SHOES = "sec_spec_2264-Shoes";
	@XmlElementDecl(name = SEC_SPEC_2264_SHOES)
	public JAXBElement<String> createSEC_SPEC_2264_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2264_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2265_KIDS = "sec_spec_2265-Kids";
	@XmlElementDecl(name = SEC_SPEC_2265_KIDS)
	public JAXBElement<String> createSEC_SPEC_2265_KIDS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2265_KIDS), String.class, attribute);
	}

	public static final String SEC_SPEC_2267_ATHLETIC_SHOES = "sec_spec_2267-Athletic_Shoes";
	@XmlElementDecl(name = SEC_SPEC_2267_ATHLETIC_SHOES)
	public JAXBElement<String> createSEC_SPEC_2267_ATHLETIC_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2267_ATHLETIC_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2268_BOOTS = "sec_spec_2268-Boots";
	@XmlElementDecl(name = SEC_SPEC_2268_BOOTS)
	public JAXBElement<String> createSEC_SPEC_2268_BOOTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2268_BOOTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2272_SLIPPERS = "sec_spec_2272-Slippers";
	@XmlElementDecl(name = SEC_SPEC_2272_SLIPPERS)
	public JAXBElement<String> createSEC_SPEC_2272_SLIPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2272_SLIPPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2274_ATHLETIC_SHOES = "sec_spec_2274-Athletic_Shoes";
	@XmlElementDecl(name = SEC_SPEC_2274_ATHLETIC_SHOES)
	public JAXBElement<String> createSEC_SPEC_2274_ATHLETIC_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2274_ATHLETIC_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2275_BOOTS = "sec_spec_2275-Boots";
	@XmlElementDecl(name = SEC_SPEC_2275_BOOTS)
	public JAXBElement<String> createSEC_SPEC_2275_BOOTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2275_BOOTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2276_CASUAL_SHOES = "sec_spec_2276-Casual_Shoes";
	@XmlElementDecl(name = SEC_SPEC_2276_CASUAL_SHOES)
	public JAXBElement<String> createSEC_SPEC_2276_CASUAL_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2276_CASUAL_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2277_DRESS_SHOES = "sec_spec_2277-Dress_Shoes";
	@XmlElementDecl(name = SEC_SPEC_2277_DRESS_SHOES)
	public JAXBElement<String> createSEC_SPEC_2277_DRESS_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2277_DRESS_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2278_SANDALS = "sec_spec_2278-Sandals";
	@XmlElementDecl(name = SEC_SPEC_2278_SANDALS)
	public JAXBElement<String> createSEC_SPEC_2278_SANDALS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2278_SANDALS), String.class, attribute);
	}

	public static final String SEC_SPEC_2279_SLIPPERS = "sec_spec_2279-Slippers";
	@XmlElementDecl(name = SEC_SPEC_2279_SLIPPERS)
	public JAXBElement<String> createSEC_SPEC_2279_SLIPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2279_SLIPPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2280_MEN = "sec_spec_2280-Men";
	@XmlElementDecl(name = SEC_SPEC_2280_MEN)
	public JAXBElement<String> createSEC_SPEC_2280_MEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2280_MEN), String.class, attribute);
	}

	public static final String SEC_SPEC_2281_ATHLETIC_SHOES = "sec_spec_2281-Athletic_Shoes";
	@XmlElementDecl(name = SEC_SPEC_2281_ATHLETIC_SHOES)
	public JAXBElement<String> createSEC_SPEC_2281_ATHLETIC_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2281_ATHLETIC_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2282_BOOTS = "sec_spec_2282-Boots";
	@XmlElementDecl(name = SEC_SPEC_2282_BOOTS)
	public JAXBElement<String> createSEC_SPEC_2282_BOOTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2282_BOOTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2286_SLIPPERS = "sec_spec_2286-Slippers";
	@XmlElementDecl(name = SEC_SPEC_2286_SLIPPERS)
	public JAXBElement<String> createSEC_SPEC_2286_SLIPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2286_SLIPPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2287_WOMEN = "sec_spec_2287-Women";
	@XmlElementDecl(name = SEC_SPEC_2287_WOMEN)
	public JAXBElement<String> createSEC_SPEC_2287_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2287_WOMEN), String.class, attribute);
	}

	public static final String SEC_SPEC_2288_ATHLETIC_SHOES = "sec_spec_2288-Athletic_Shoes";
	@XmlElementDecl(name = SEC_SPEC_2288_ATHLETIC_SHOES)
	public JAXBElement<String> createSEC_SPEC_2288_ATHLETIC_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2288_ATHLETIC_SHOES), String.class, attribute);
	}

	public static final String SEC_SPEC_2289_BOOTS = "sec_spec_2289-Boots";
	@XmlElementDecl(name = SEC_SPEC_2289_BOOTS)
	public JAXBElement<String> createSEC_SPEC_2289_BOOTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2289_BOOTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2290_FLATS = "sec_spec_2290-Flats";
	@XmlElementDecl(name = SEC_SPEC_2290_FLATS)
	public JAXBElement<String> createSEC_SPEC_2290_FLATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2290_FLATS), String.class, attribute);
	}

	public static final String SEC_SPEC_2291_HEELS = "sec_spec_2291-Heels";
	@XmlElementDecl(name = SEC_SPEC_2291_HEELS)
	public JAXBElement<String> createSEC_SPEC_2291_HEELS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2291_HEELS), String.class, attribute);
	}

	public static final String SEC_SPEC_2292_SANDALS = "sec_spec_2292-Sandals";
	@XmlElementDecl(name = SEC_SPEC_2292_SANDALS)
	public JAXBElement<String> createSEC_SPEC_2292_SANDALS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2292_SANDALS), String.class, attribute);
	}

	public static final String SEC_SPEC_2293_SLIPPERS = "sec_spec_2293-Slippers";
	@XmlElementDecl(name = SEC_SPEC_2293_SLIPPERS)
	public JAXBElement<String> createSEC_SPEC_2293_SLIPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2293_SLIPPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2294_WOMEN = "sec_spec_2294-Women";
	@XmlElementDecl(name = SEC_SPEC_2294_WOMEN)
	public JAXBElement<String> createSEC_SPEC_2294_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2294_WOMEN), String.class, attribute);
	}

	public static final String SEC_SPEC_2295_COATS = "sec_spec_2295-Coats";
	@XmlElementDecl(name = SEC_SPEC_2295_COATS)
	public JAXBElement<String> createSEC_SPEC_2295_COATS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2295_COATS), String.class, attribute);
	}

	public static final String SEC_SPEC_2304_DRESSES = "sec_spec_2304-Dresses";
	@XmlElementDecl(name = SEC_SPEC_2304_DRESSES)
	public JAXBElement<String> createSEC_SPEC_2304_DRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2304_DRESSES), String.class, attribute);
	}

	public static final String SEC_SPEC_2305_JACKETS_AND_BLAZERS = "sec_spec_2305-Jackets_and_Blazers";
	@XmlElementDecl(name = SEC_SPEC_2305_JACKETS_AND_BLAZERS)
	public JAXBElement<String> createSEC_SPEC_2305_JACKETS_AND_BLAZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2305_JACKETS_AND_BLAZERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2306_JEANS = "sec_spec_2306-Jeans";
	@XmlElementDecl(name = SEC_SPEC_2306_JEANS)
	public JAXBElement<String> createSEC_SPEC_2306_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2306_JEANS), String.class, attribute);
	}

	public static final String SEC_SPEC_2307_JUMPSUITS_AND_ROMPERS = "sec_spec_2307-Jumpsuits_and_Rompers";
	@XmlElementDecl(name = SEC_SPEC_2307_JUMPSUITS_AND_ROMPERS)
	public JAXBElement<String> createSEC_SPEC_2307_JUMPSUITS_AND_ROMPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2307_JUMPSUITS_AND_ROMPERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2309_BRAS = "sec_spec_2309-Bras";
	@XmlElementDecl(name = SEC_SPEC_2309_BRAS)
	public JAXBElement<String> createSEC_SPEC_2309_BRAS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2309_BRAS), String.class, attribute);
	}

	public static final String SEC_SPEC_2324_PANTIES = "sec_spec_2324-Panties";
	@XmlElementDecl(name = SEC_SPEC_2324_PANTIES)
	public JAXBElement<String> createSEC_SPEC_2324_PANTIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2324_PANTIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2332_SHAPEWEAR = "sec_spec_2332-Shapewear";
	@XmlElementDecl(name = SEC_SPEC_2332_SHAPEWEAR)
	public JAXBElement<String> createSEC_SPEC_2332_SHAPEWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2332_SHAPEWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_2338_SLIPS_AND_CAMISOLES = "sec_spec_2338-Slips_and_Camisoles";
	@XmlElementDecl(name = SEC_SPEC_2338_SLIPS_AND_CAMISOLES)
	public JAXBElement<String> createSEC_SPEC_2338_SLIPS_AND_CAMISOLES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2338_SLIPS_AND_CAMISOLES), String.class, attribute);
	}

	public static final String SEC_SPEC_2340_SLIPS = "sec_spec_2340-Slips";
	@XmlElementDecl(name = SEC_SPEC_2340_SLIPS)
	public JAXBElement<String> createSEC_SPEC_2340_SLIPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2340_SLIPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2342_PANTS_AND_LEGGINGS = "sec_spec_2342-Pants_and_Leggings";
	@XmlElementDecl(name = SEC_SPEC_2342_PANTS_AND_LEGGINGS)
	public JAXBElement<String> createSEC_SPEC_2342_PANTS_AND_LEGGINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2342_PANTS_AND_LEGGINGS), String.class, attribute);
	}

	public static final String SEC_SPEC_2347_TROUSERS = "sec_spec_2347-Trousers";
	@XmlElementDecl(name = SEC_SPEC_2347_TROUSERS)
	public JAXBElement<String> createSEC_SPEC_2347_TROUSERS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2347_TROUSERS), String.class, attribute);
	}

	public static final String SEC_SPEC_2348_SHORTS = "sec_spec_2348-Shorts";
	@XmlElementDecl(name = SEC_SPEC_2348_SHORTS)
	public JAXBElement<String> createSEC_SPEC_2348_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2348_SHORTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2349_SKIRTS = "sec_spec_2349-Skirts";
	@XmlElementDecl(name = SEC_SPEC_2349_SKIRTS)
	public JAXBElement<String> createSEC_SPEC_2349_SKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2349_SKIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR = "sec_spec_2350-Sleepwear_and_Loungewear";
	@XmlElementDecl(name = SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR)
	public JAXBElement<String> createSEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_2351_BABYDOLLS_AND_CHEMISES = "sec_spec_2351-Babydolls_and_Chemises";
	@XmlElementDecl(name = SEC_SPEC_2351_BABYDOLLS_AND_CHEMISES)
	public JAXBElement<String> createSEC_SPEC_2351_BABYDOLLS_AND_CHEMISES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2351_BABYDOLLS_AND_CHEMISES), String.class, attribute);
	}

	public static final String SEC_SPEC_2352_NIGHTGOWNS_AND_SLEEP_SHIRTS = "sec_spec_2352-Nightgowns_and_Sleep_Shirts";
	@XmlElementDecl(name = SEC_SPEC_2352_NIGHTGOWNS_AND_SLEEP_SHIRTS)
	public JAXBElement<String> createSEC_SPEC_2352_NIGHTGOWNS_AND_SLEEP_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2352_NIGHTGOWNS_AND_SLEEP_SHIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2353_PAJAMA_SETS = "sec_spec_2353-Pajama_Sets";
	@XmlElementDecl(name = SEC_SPEC_2353_PAJAMA_SETS)
	public JAXBElement<String> createSEC_SPEC_2353_PAJAMA_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2353_PAJAMA_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2354_PANTS = "sec_spec_2354-Pants";
	@XmlElementDecl(name = SEC_SPEC_2354_PANTS)
	public JAXBElement<String> createSEC_SPEC_2354_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2354_PANTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2355_ROBES = "sec_spec_2355-Robes";
	@XmlElementDecl(name = SEC_SPEC_2355_ROBES)
	public JAXBElement<String> createSEC_SPEC_2355_ROBES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2355_ROBES), String.class, attribute);
	}

	public static final String SEC_SPEC_2356_TOPS = "sec_spec_2356-Tops";
	@XmlElementDecl(name = SEC_SPEC_2356_TOPS)
	public JAXBElement<String> createSEC_SPEC_2356_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2356_TOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2357_SUITS_AND_SEPARATES = "sec_spec_2357-Suits_and_Separates";
	@XmlElementDecl(name = SEC_SPEC_2357_SUITS_AND_SEPARATES)
	public JAXBElement<String> createSEC_SPEC_2357_SUITS_AND_SEPARATES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2357_SUITS_AND_SEPARATES), String.class, attribute);
	}

	public static final String SEC_SPEC_2358_DRESS_SUIT_SETS = "sec_spec_2358-Dress_Suit_Sets";
	@XmlElementDecl(name = SEC_SPEC_2358_DRESS_SUIT_SETS)
	public JAXBElement<String> createSEC_SPEC_2358_DRESS_SUIT_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2358_DRESS_SUIT_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2359_PANT_SUIT_SETS = "sec_spec_2359-Pant_Suit_Sets";
	@XmlElementDecl(name = SEC_SPEC_2359_PANT_SUIT_SETS)
	public JAXBElement<String> createSEC_SPEC_2359_PANT_SUIT_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2359_PANT_SUIT_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2360_SKIRT_SUIT_SETS = "sec_spec_2360-Skirt_Suit_Sets";
	@XmlElementDecl(name = SEC_SPEC_2360_SKIRT_SUIT_SETS)
	public JAXBElement<String> createSEC_SPEC_2360_SKIRT_SUIT_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2360_SKIRT_SUIT_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_2362_SUIT_DRESSES = "sec_spec_2362-Suit_Dresses";
	@XmlElementDecl(name = SEC_SPEC_2362_SUIT_DRESSES)
	public JAXBElement<String> createSEC_SPEC_2362_SUIT_DRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2362_SUIT_DRESSES), String.class, attribute);
	}

	public static final String SEC_SPEC_2364_SUIT_PANTS = "sec_spec_2364-Suit_Pants";
	@XmlElementDecl(name = SEC_SPEC_2364_SUIT_PANTS)
	public JAXBElement<String> createSEC_SPEC_2364_SUIT_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2364_SUIT_PANTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2365_SUIT_SKIRTS = "sec_spec_2365-Suit_Skirts";
	@XmlElementDecl(name = SEC_SPEC_2365_SUIT_SKIRTS)
	public JAXBElement<String> createSEC_SPEC_2365_SUIT_SKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2365_SUIT_SKIRTS), String.class, attribute);
	}

	public static final String SEC_SPEC_2366_SWEATERS_AND_HOODIES = "sec_spec_2366-Sweaters_and_Hoodies";
	@XmlElementDecl(name = SEC_SPEC_2366_SWEATERS_AND_HOODIES)
	public JAXBElement<String> createSEC_SPEC_2366_SWEATERS_AND_HOODIES(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2366_SWEATERS_AND_HOODIES), String.class, attribute);
	}

	public static final String SEC_SPEC_2367_SWIMWEAR = "sec_spec_2367-Swimwear";
	@XmlElementDecl(name = SEC_SPEC_2367_SWIMWEAR)
	public JAXBElement<String> createSEC_SPEC_2367_SWIMWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2367_SWIMWEAR), String.class, attribute);
	}

	public static final String SEC_SPEC_2368_COVER_UPS = "sec_spec_2368-Cover_Ups";
	@XmlElementDecl(name = SEC_SPEC_2368_COVER_UPS)
	public JAXBElement<String> createSEC_SPEC_2368_COVER_UPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2368_COVER_UPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2369_ONE_PIECE = "sec_spec_2369-One_Piece";
	@XmlElementDecl(name = SEC_SPEC_2369_ONE_PIECE)
	public JAXBElement<String> createSEC_SPEC_2369_ONE_PIECE(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2369_ONE_PIECE), String.class, attribute);
	}

	public static final String SEC_SPEC_2370_TWO_PIECE_BOTTOMS = "sec_spec_2370-Two_Piece_Bottoms";
	@XmlElementDecl(name = SEC_SPEC_2370_TWO_PIECE_BOTTOMS)
	public JAXBElement<String> createSEC_SPEC_2370_TWO_PIECE_BOTTOMS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2370_TWO_PIECE_BOTTOMS), String.class, attribute);
	}

	public static final String SEC_SPEC_2371_TWO_PIECE_TOPS = "sec_spec_2371-Two_Piece_Tops";
	@XmlElementDecl(name = SEC_SPEC_2371_TWO_PIECE_TOPS)
	public JAXBElement<String> createSEC_SPEC_2371_TWO_PIECE_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2371_TWO_PIECE_TOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_2372_TOPS = "sec_spec_2372-Tops";
	@XmlElementDecl(name = SEC_SPEC_2372_TOPS)
	public JAXBElement<String> createSEC_SPEC_2372_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_2372_TOPS), String.class, attribute);
	}

	public static final String SEC_SPEC_3201_SETS = "sec_spec_3201-Sets";
	@XmlElementDecl(name = SEC_SPEC_3201_SETS)
	public JAXBElement<String> createSEC_SPEC_3201_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_3201_SETS), String.class, attribute);
	}

	public static final String SEC_SPEC_3202_SETS = "sec_spec_3202-Sets";
	@XmlElementDecl(name = SEC_SPEC_3202_SETS)
	public JAXBElement<String> createSEC_SPEC_3202_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_3202_SETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1601_ACCESSORIES = "zbm_sec_spec_1601_Accessories";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1601_ACCESSORIES)
	public JAXBElement<String> createZBM_SEC_SPEC_1601_ACCESSORIES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1601_ACCESSORIES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1603_BOYS = "zbm_sec_spec_1603_Boys";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1603_BOYS)
	public JAXBElement<String> createZBM_SEC_SPEC_1603_BOYS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1603_BOYS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1620_GLASSES = "zbm_sec_spec_1620_Glasses";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1620_GLASSES)
	public JAXBElement<String> createZBM_SEC_SPEC_1620_GLASSES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1620_GLASSES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1622_SUNGLASSES = "zbm_sec_spec_1622_Sunglasses";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1622_SUNGLASSES)
	public JAXBElement<String> createZBM_SEC_SPEC_1622_SUNGLASSES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1622_SUNGLASSES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1623_JEWELRY = "zbm_sec_spec_1623_Jewelry";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1623_JEWELRY)
	public JAXBElement<String> createZBM_SEC_SPEC_1623_JEWELRY(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1623_JEWELRY), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1626_BRACELETS = "zbm_sec_spec_1626_Bracelets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1626_BRACELETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1626_BRACELETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1626_BRACELETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1628_EARRINGS = "zbm_sec_spec_1628_Earrings";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1628_EARRINGS)
	public JAXBElement<String> createZBM_SEC_SPEC_1628_EARRINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1628_EARRINGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1631_NECKLACES = "zbm_sec_spec_1631_Necklaces";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1631_NECKLACES)
	public JAXBElement<String> createZBM_SEC_SPEC_1631_NECKLACES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1631_NECKLACES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1633_RINGS = "zbm_sec_spec_1633_Rings";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1633_RINGS)
	public JAXBElement<String> createZBM_SEC_SPEC_1633_RINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1633_RINGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1635_WATCHES = "zbm_sec_spec_1635_Watches";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1635_WATCHES)
	public JAXBElement<String> createZBM_SEC_SPEC_1635_WATCHES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1635_WATCHES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1636_LUGGAGE = "zbm_sec_spec_1636_Luggage";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1636_LUGGAGE)
	public JAXBElement<String> createZBM_SEC_SPEC_1636_LUGGAGE(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1636_LUGGAGE), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1673_WALLETS = "zbm_sec_spec_1673_Wallets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1673_WALLETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1673_WALLETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1673_WALLETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1679_WOMEN = "zbm_sec_spec_1679_Women";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1679_WOMEN)
	public JAXBElement<String> createZBM_SEC_SPEC_1679_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1679_WOMEN), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1681_GLOVES = "zbm_sec_spec_1681_Gloves";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1681_GLOVES)
	public JAXBElement<String> createZBM_SEC_SPEC_1681_GLOVES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1681_GLOVES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1683_HANDBAGS = "zbm_sec_spec_1683_Handbags";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1683_HANDBAGS)
	public JAXBElement<String> createZBM_SEC_SPEC_1683_HANDBAGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1683_HANDBAGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1688_DIAPER_BAGS = "zbm_sec_spec_1688_Diaper_Bags";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1688_DIAPER_BAGS)
	public JAXBElement<String> createZBM_SEC_SPEC_1688_DIAPER_BAGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1688_DIAPER_BAGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1694_HOSIERY_AND_SOCKS = "zbm_sec_spec_1694_Hosiery_and_Socks";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1694_HOSIERY_AND_SOCKS)
	public JAXBElement<String> createZBM_SEC_SPEC_1694_HOSIERY_AND_SOCKS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1694_HOSIERY_AND_SOCKS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE = "zbm_sec_spec_1696_Tights_and_Pantyhose";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE)
	public JAXBElement<String> createZBM_SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1696_TIGHTS_AND_PANTYHOSE), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1699_BABY_AND_KIDS = "zbm_sec_spec_1699_Baby_and_Kids";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1699_BABY_AND_KIDS)
	public JAXBElement<String> createZBM_SEC_SPEC_1699_BABY_AND_KIDS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1699_BABY_AND_KIDS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1701_BABY_GEAR = "zbm_sec_spec_1701_Baby_Gear";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1701_BABY_GEAR)
	public JAXBElement<String> createZBM_SEC_SPEC_1701_BABY_GEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1701_BABY_GEAR), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1745_PAJAMAS = "zbm_sec_spec_1745_Pajamas";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1745_PAJAMAS)
	public JAXBElement<String> createZBM_SEC_SPEC_1745_PAJAMAS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1745_PAJAMAS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1760_PAJAMAS = "zbm_sec_spec_1760_Pajamas";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1760_PAJAMAS)
	public JAXBElement<String> createZBM_SEC_SPEC_1760_PAJAMAS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1760_PAJAMAS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1774_BEAUTY = "zbm_sec_spec_1774_Beauty";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1774_BEAUTY)
	public JAXBElement<String> createZBM_SEC_SPEC_1774_BEAUTY(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1774_BEAUTY), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1775_BATH_AND_BODY = "zbm_sec_spec_1775_Bath_and_Body";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1775_BATH_AND_BODY)
	public JAXBElement<String> createZBM_SEC_SPEC_1775_BATH_AND_BODY(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1775_BATH_AND_BODY), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1788_GIFT_AND_VALUE_SETS = "zbm_sec_spec_1788_Gift_and_Value_Sets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1788_GIFT_AND_VALUE_SETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1788_GIFT_AND_VALUE_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1788_GIFT_AND_VALUE_SETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1805_MAKEUP = "zbm_sec_spec_1805_Makeup";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1805_MAKEUP)
	public JAXBElement<String> createZBM_SEC_SPEC_1805_MAKEUP(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1805_MAKEUP), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1834_SKIN_CARE = "zbm_sec_spec_1834_Skin_Care";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1834_SKIN_CARE)
	public JAXBElement<String> createZBM_SEC_SPEC_1834_SKIN_CARE(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1834_SKIN_CARE), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1869_FOR_THE_HOME = "zbm_sec_spec_1869_For_the_Home";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1869_FOR_THE_HOME)
	public JAXBElement<String> createZBM_SEC_SPEC_1869_FOR_THE_HOME(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1869_FOR_THE_HOME), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1872_BATH_ACCESSORY_SETS = "zbm_sec_spec_1872_Bath_Accessory_Sets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1872_BATH_ACCESSORY_SETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1872_BATH_ACCESSORY_SETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1872_BATH_ACCESSORY_SETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1873_BATH_MATS_AND_RUGS = "zbm_sec_spec_1873_Bath_Mats_and_Rugs";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1873_BATH_MATS_AND_RUGS)
	public JAXBElement<String> createZBM_SEC_SPEC_1873_BATH_MATS_AND_RUGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1873_BATH_MATS_AND_RUGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1875_BATH_TOWELS = "zbm_sec_spec_1875_Bath_Towels";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1875_BATH_TOWELS)
	public JAXBElement<String> createZBM_SEC_SPEC_1875_BATH_TOWELS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1875_BATH_TOWELS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1881_BEDSPREADS_AND_COVERLETS = "zbm_sec_spec_1881_Bedspreads_and_Coverlets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1881_BEDSPREADS_AND_COVERLETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1881_BEDSPREADS_AND_COVERLETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1881_BEDSPREADS_AND_COVERLETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1882_BLANKETS = "zbm_sec_spec_1882_Blankets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1882_BLANKETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1882_BLANKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1882_BLANKETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS = "zbm_sec_spec_1886_Mattress_Pads_and_Toppers";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS)
	public JAXBElement<String> createZBM_SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1886_MATTRESS_PADS_AND_TOPPERS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1887_PILLOWS = "zbm_sec_spec_1887_Pillows";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1887_PILLOWS)
	public JAXBElement<String> createZBM_SEC_SPEC_1887_PILLOWS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1887_PILLOWS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1889_SHEETS = "zbm_sec_spec_1889_Sheets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1889_SHEETS)
	public JAXBElement<String> createZBM_SEC_SPEC_1889_SHEETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1889_SHEETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1899_DINING = "zbm_sec_spec_1899_Dining";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1899_DINING)
	public JAXBElement<String> createZBM_SEC_SPEC_1899_DINING(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1899_DINING), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1949_TABLE_LINENS = "zbm_sec_spec_1949_Table_Linens";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1949_TABLE_LINENS)
	public JAXBElement<String> createZBM_SEC_SPEC_1949_TABLE_LINENS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1949_TABLE_LINENS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_1970_FURNITURE = "zbm_sec_spec_1970_Furniture";
	@XmlElementDecl(name = ZBM_SEC_SPEC_1970_FURNITURE)
	public JAXBElement<String> createZBM_SEC_SPEC_1970_FURNITURE(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_1970_FURNITURE), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2149_WINDOW_TREATMENTS = "zbm_sec_spec_2149_Window_Treatments";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2149_WINDOW_TREATMENTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2149_WINDOW_TREATMENTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2149_WINDOW_TREATMENTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2157_JUNIORS = "zbm_sec_spec_2157_Juniors";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2157_JUNIORS)
	public JAXBElement<String> createZBM_SEC_SPEC_2157_JUNIORS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2157_JUNIORS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2167_DRESSES = "zbm_sec_spec_2167_Dresses";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2167_DRESSES)
	public JAXBElement<String> createZBM_SEC_SPEC_2167_DRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2167_DRESSES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2168_JACKETS_AND_BLAZERS = "zbm_sec_spec_2168_Jackets_and_Blazers";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2168_JACKETS_AND_BLAZERS)
	public JAXBElement<String> createZBM_SEC_SPEC_2168_JACKETS_AND_BLAZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2168_JACKETS_AND_BLAZERS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2169_JEANS = "zbm_sec_spec_2169_Jeans";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2169_JEANS)
	public JAXBElement<String> createZBM_SEC_SPEC_2169_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2169_JEANS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2190_PANTS_AND_LEGGINGS = "zbm_sec_spec_2190_Pants_and_Leggings";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2190_PANTS_AND_LEGGINGS)
	public JAXBElement<String> createZBM_SEC_SPEC_2190_PANTS_AND_LEGGINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2190_PANTS_AND_LEGGINGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2196_SKIRTS = "zbm_sec_spec_2196_Skirts";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2196_SKIRTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2196_SKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2196_SKIRTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2204_SWEATERS_AND_HOODIES = "zbm_sec_spec_2204_Sweaters_and_Hoodies";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2204_SWEATERS_AND_HOODIES)
	public JAXBElement<String> createZBM_SEC_SPEC_2204_SWEATERS_AND_HOODIES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2204_SWEATERS_AND_HOODIES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2210_TOPS = "zbm_sec_spec_2210_Tops";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2210_TOPS)
	public JAXBElement<String> createZBM_SEC_SPEC_2210_TOPS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2210_TOPS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2217_MEN = "zbm_sec_spec_2217_Men";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2217_MEN)
	public JAXBElement<String> createZBM_SEC_SPEC_2217_MEN(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2217_MEN), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2218_COATS_AND_JACKETS = "zbm_sec_spec_2218_Coats_and_Jackets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2218_COATS_AND_JACKETS)
	public JAXBElement<String> createZBM_SEC_SPEC_2218_COATS_AND_JACKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2218_COATS_AND_JACKETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2226_PANTS = "zbm_sec_spec_2226_Pants";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2226_PANTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2226_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2226_PANTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2234_DRESS_SHIRTS = "zbm_sec_spec_2234_Dress_Shirts";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2234_DRESS_SHIRTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2234_DRESS_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2234_DRESS_SHIRTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2236_T_SHIRTS = "zbm_sec_spec_2236_T_Shirts";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2236_T_SHIRTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2236_T_SHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2236_T_SHIRTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2239_LOUNGEWEAR = "zbm_sec_spec_2239_Loungewear";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2239_LOUNGEWEAR)
	public JAXBElement<String> createZBM_SEC_SPEC_2239_LOUNGEWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2239_LOUNGEWEAR), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2240_PAJAMAS = "zbm_sec_spec_2240_Pajamas";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2240_PAJAMAS)
	public JAXBElement<String> createZBM_SEC_SPEC_2240_PAJAMAS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2240_PAJAMAS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2241_ROBES = "zbm_sec_spec_2241_Robes";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2241_ROBES)
	public JAXBElement<String> createZBM_SEC_SPEC_2241_ROBES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2241_ROBES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2249_SWEATERS = "zbm_sec_spec_2249_Sweaters";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2249_SWEATERS)
	public JAXBElement<String> createZBM_SEC_SPEC_2249_SWEATERS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2249_SWEATERS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2254_SWIMWEAR = "zbm_sec_spec_2254_Swimwear";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2254_SWIMWEAR)
	public JAXBElement<String> createZBM_SEC_SPEC_2254_SWIMWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2254_SWIMWEAR), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2258_UNDERWEAR = "zbm_sec_spec_2258_Underwear";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2258_UNDERWEAR)
	public JAXBElement<String> createZBM_SEC_SPEC_2258_UNDERWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2258_UNDERWEAR), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2264_SHOES = "zbm_sec_spec_2264_Shoes";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2264_SHOES)
	public JAXBElement<String> createZBM_SEC_SPEC_2264_SHOES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2264_SHOES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2265_KIDS = "zbm_sec_spec_2265_Kids";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2265_KIDS)
	public JAXBElement<String> createZBM_SEC_SPEC_2265_KIDS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2265_KIDS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2280_MEN = "zbm_sec_spec_2280_Men";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2280_MEN)
	public JAXBElement<String> createZBM_SEC_SPEC_2280_MEN(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2280_MEN), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2287_WOMEN = "zbm_sec_spec_2287_Women";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2287_WOMEN)
	public JAXBElement<String> createZBM_SEC_SPEC_2287_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2287_WOMEN), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2294_WOMEN = "zbm_sec_spec_2294_Women";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2294_WOMEN)
	public JAXBElement<String> createZBM_SEC_SPEC_2294_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2294_WOMEN), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2295_COATS = "zbm_sec_spec_2295_Coats";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2295_COATS)
	public JAXBElement<String> createZBM_SEC_SPEC_2295_COATS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2295_COATS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2303_VEST = "zbm_sec_spec_2303_Vest";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2303_VEST)
	public JAXBElement<String> createZBM_SEC_SPEC_2303_VEST(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2303_VEST), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2304_DRESSES = "zbm_sec_spec_2304_Dresses";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2304_DRESSES)
	public JAXBElement<String> createZBM_SEC_SPEC_2304_DRESSES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2304_DRESSES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2305_JACKETS_AND_BLAZERS = "zbm_sec_spec_2305_Jackets_and_Blazers";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2305_JACKETS_AND_BLAZERS)
	public JAXBElement<String> createZBM_SEC_SPEC_2305_JACKETS_AND_BLAZERS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2305_JACKETS_AND_BLAZERS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2306_JEANS = "zbm_sec_spec_2306_Jeans";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2306_JEANS)
	public JAXBElement<String> createZBM_SEC_SPEC_2306_JEANS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2306_JEANS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2309_BRAS = "zbm_sec_spec_2309_Bras";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2309_BRAS)
	public JAXBElement<String> createZBM_SEC_SPEC_2309_BRAS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2309_BRAS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2324_PANTIES = "zbm_sec_spec_2324_Panties";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2324_PANTIES)
	public JAXBElement<String> createZBM_SEC_SPEC_2324_PANTIES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2324_PANTIES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2332_SHAPEWEAR = "zbm_sec_spec_2332_Shapewear";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2332_SHAPEWEAR)
	public JAXBElement<String> createZBM_SEC_SPEC_2332_SHAPEWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2332_SHAPEWEAR), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2338_SLIPS_AND_CAMISOLES = "zbm_sec_spec_2338_Slips_and_Camisoles";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2338_SLIPS_AND_CAMISOLES)
	public JAXBElement<String> createZBM_SEC_SPEC_2338_SLIPS_AND_CAMISOLES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2338_SLIPS_AND_CAMISOLES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2342_PANTS_AND_LEGGINGS = "zbm_sec_spec_2342_Pants_and_Leggings";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2342_PANTS_AND_LEGGINGS)
	public JAXBElement<String> createZBM_SEC_SPEC_2342_PANTS_AND_LEGGINGS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2342_PANTS_AND_LEGGINGS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2343_ACTIVE_AND_YOGA_PANTS = "zbm_sec_spec_2343_Active_and_Yoga_Pants";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2343_ACTIVE_AND_YOGA_PANTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2343_ACTIVE_AND_YOGA_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2343_ACTIVE_AND_YOGA_PANTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2344_CAPRI_PANTS = "zbm_sec_spec_2344_Capri_Pants";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2344_CAPRI_PANTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2344_CAPRI_PANTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2344_CAPRI_PANTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2349_SKIRTS = "zbm_sec_spec_2349_Skirts";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2349_SKIRTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2349_SKIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2349_SKIRTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR = "zbm_sec_spec_2350_Sleepwear_and_Loungewear";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR)
	public JAXBElement<String> createZBM_SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2350_SLEEPWEAR_AND_LOUNGEWEAR), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2355_ROBES = "zbm_sec_spec_2355_Robes";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2355_ROBES)
	public JAXBElement<String> createZBM_SEC_SPEC_2355_ROBES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2355_ROBES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2357_SUITS_AND_SEPARATES = "zbm_sec_spec_2357_Suits_and_Separates";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2357_SUITS_AND_SEPARATES)
	public JAXBElement<String> createZBM_SEC_SPEC_2357_SUITS_AND_SEPARATES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2357_SUITS_AND_SEPARATES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2361_SUIT_SEPARATES = "zbm_sec_spec_2361_Suit_Separates";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2361_SUIT_SEPARATES)
	public JAXBElement<String> createZBM_SEC_SPEC_2361_SUIT_SEPARATES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2361_SUIT_SEPARATES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2363_SUIT_JACKETS = "zbm_sec_spec_2363_Suit_Jackets";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2363_SUIT_JACKETS)
	public JAXBElement<String> createZBM_SEC_SPEC_2363_SUIT_JACKETS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2363_SUIT_JACKETS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2366_SWEATERS_AND_HOODIES = "zbm_sec_spec_2366_Sweaters_and_Hoodies";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2366_SWEATERS_AND_HOODIES)
	public JAXBElement<String> createZBM_SEC_SPEC_2366_SWEATERS_AND_HOODIES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2366_SWEATERS_AND_HOODIES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2368_COVER_UPS = "zbm_sec_spec_2368_Cover_Ups";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2368_COVER_UPS)
	public JAXBElement<String> createZBM_SEC_SPEC_2368_COVER_UPS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2368_COVER_UPS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2373_BLOUSES = "zbm_sec_spec_2373_Blouses";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2373_BLOUSES)
	public JAXBElement<String> createZBM_SEC_SPEC_2373_BLOUSES(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2373_BLOUSES), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_2377_TSHIRTS = "zbm_sec_spec_2377_TShirts";
	@XmlElementDecl(name = ZBM_SEC_SPEC_2377_TSHIRTS)
	public JAXBElement<String> createZBM_SEC_SPEC_2377_TSHIRTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_2377_TSHIRTS), String.class, attribute);
	}

	public static final String ITEM_UDA_SPEC = "Item_UDA_Spec";
	@XmlElementDecl(name = ITEM_UDA_SPEC)
	public JAXBElement<String> createITEM_UDA_SPEC(String attribute) { 
	    return new JAXBElement<String>(new QName(ITEM_UDA_SPEC), String.class, attribute);
	}

	public static final String ECOMM_STYLE_SPEC = "Ecomm_Style_Spec";
	@XmlElementDecl(name = ECOMM_STYLE_SPEC)
	public JAXBElement<String> createECOMM_STYLE_SPEC(String attribute) { 
	    return new JAXBElement<String>(new QName(ECOMM_STYLE_SPEC), String.class, attribute);
	}
	public static final String ECOMM_PACKCOLOR_SPEC = "Ecomm_PackColor_Spec";
	@XmlElementDecl(name = ECOMM_PACKCOLOR_SPEC)
	public JAXBElement<String> createECOMM_PACKCOLOR_SPEC(String attribute) { 
	    return new JAXBElement<String>(new QName(ECOMM_PACKCOLOR_SPEC), String.class, attribute);
	}
	
	public static final String ECOMM_SKU_SPEC = "Ecomm_SKU_Spec";
	@XmlElementDecl(name = ECOMM_SKU_SPEC)
	public JAXBElement<String> createECOMM_SKU_SPEC(String attribute) { 
	    return new JAXBElement<String>(new QName(ECOMM_SKU_SPEC), String.class, attribute);
	}
	
}