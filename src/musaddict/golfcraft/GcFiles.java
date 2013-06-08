package musaddict.golfcraft;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class GcFiles {

	private static File HoleFile = new File(Golfcraft.mainDirectory + File.separator + "Holes.xml");

	private static ArrayList <GcHole> HoleList;


	/**
	 * Loads all doors from disk.
	 */
	public static void load() {
		Golfcraft.Log(Level.INFO, "Loading Holes...");

		if (HoleList == null)
			HoleList = new ArrayList <GcHole>();

		if (!HoleFile.exists())
			Golfcraft.Log("No Holes to load.");
		else {
			HoleList = new ArrayList <GcHole>();
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			try {
				docBuilder = dbfac.newDocumentBuilder();
			}
			catch (ParserConfigurationException e) {}
			Document doc = null;

			try {
				doc = docBuilder.parse(HoleFile);
				doc.getDocumentElement().normalize();
			}
			catch (IOException | SAXException e) {
				Golfcraft.Log(Level.SEVERE, "Holes file is malformed.");

				e.printStackTrace();

				return;
			}

			try {
				@SuppressWarnings("unused")
				Double fileVersion = -1.0;

				NodeList fileVersionNodeList = doc.getElementsByTagName("file");

				if (fileVersionNodeList.getLength() > 0) {
					for (int i = 0; i < fileVersionNodeList.getLength(); i++) {
						Node fileVersionNode = fileVersionNodeList.item(i);

						if (fileVersionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element fileVersionElement = (Element) fileVersionNode;

							fileVersion = Double.parseDouble(fileVersionElement.getAttribute("version"));
						}
					}
				}
				else {
					fileVersion = 1.0;
				}

				NodeList courseNodeList = doc.getElementsByTagName("hole");

				for (int i = 0; i < courseNodeList.getLength(); i++) {
					Node doorNode = courseNodeList.item(i);

					if (doorNode.getNodeType() == Node.ELEMENT_NODE) {
						Element doorElement = (Element) doorNode;

						World world = Bukkit.getWorld(doorElement.getAttribute("world"));
						String hole = doorElement.getAttribute("hole");
						int par = Integer.parseInt(doorElement.getAttribute("par"));
						int x = Integer.parseInt(doorElement.getAttribute("x")), y = Integer.parseInt(doorElement.getAttribute("y")), z = Integer.parseInt(doorElement.getAttribute("z"));

						HoleList.add(new GcHole(world, hole, par, x, y, z));
					}
				}
			}
			catch (Exception e) {
				Golfcraft.Log(Level.SEVERE, "Holes file is not in the expected format.");

				e.printStackTrace();

				return;
			}

			Golfcraft.Log(Level.INFO, "Holes loaded successfully.");
			Golfcraft.Log(Level.INFO, "Loaded " + HoleList.size() + " holes.");
		}
	}


	public static boolean save() {
		if (HoleList.size() == 0) { // If queue is empty OR economy is disabled.
			HoleFile.delete();

			Golfcraft.Log(Level.INFO, "No holes to save.");

			return true;
		}

		Golfcraft.Log(Level.INFO, "Saving " + HoleList.size() + " holes...");

		try {
			if (!HoleFile.exists()) {
				if (!HoleFile.createNewFile()) {
					Golfcraft.Log(Level.SEVERE, "Error creating Holes file.");

					return false;
				}
			}

			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element fileVersionElement = doc.createElement("file");
			fileVersionElement.setAttribute("version", "1.0");
			doc.appendChild(fileVersionElement);

			for (GcHole hole : HoleList) {
				Element doorElement = doc.createElement("hole");
				doorElement.setAttribute("world", hole.world.getName());
				doorElement.setAttribute("hole", hole.hole);
				doorElement.setAttribute("par", hole.par + "");
				doorElement.setAttribute("x", hole.x + "");
				doorElement.setAttribute("y", hole.y + "");
				doorElement.setAttribute("z", hole.z + "");
				fileVersionElement.appendChild(doorElement);
			}

			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			// trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);

			FileOutputStream OUT = new FileOutputStream(HoleFile);
			OUT.write(result.getWriter().toString().getBytes());
			OUT.flush();
			OUT.close();

			Golfcraft.Log(Level.INFO, "Holes Saved Successfully.");

			return true;
		}
		catch (Exception e) {
			Golfcraft.Log(Level.SEVERE, "Unknown error saving Holes.");

			e.printStackTrace();

			return false;
		}

	}


	/**
	 * Searches for a hole instance matching the given info and either return it or null if nothing
	 * can be found.
	 */
	public static GcHole getHole(String worldName, String holeName) {
		return getHole(new GcHole(worldName, holeName, 0)); // Par is irrelevant here as only
															// worldName and holeName are compared.
	}


	public static GcHole getHole(GcHole partial) {
		for (GcHole hole : HoleList)
			if (hole.equals(partial))
				return hole;
		return null;
	}


	public static boolean addHole(GcHole Hole) {
		if (!HoleList.contains(Hole))
			HoleList.add(Hole);
		else
			return false;

		return true;
	}


	public static boolean holeExists(Player player, String hole) {
		return getHole(player.getWorld().getName(), hole) != null;
	}


	public static boolean holeExists(Player player, String hole, int par) {
		GcHole Hole = new GcHole(player.getWorld().getName(), hole, par);

		if (HoleList.contains(Hole))
			return true;
		else
			return false;
	}

}
