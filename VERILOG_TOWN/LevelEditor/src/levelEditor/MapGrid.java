package levelEditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapGrid extends JPanel
{

	/** Generated serial version ID. */
	private static final long	serialVersionUID	= 3108997200082177130L;

	public static final String	START_NEDGE2S		= "START_NEDGE2S";
	public static final String	START_SEDGE2N		= "START_SEDGE2N";
	public static final String	START_EEDGE2W		= "START_EEDGE2W";
	public static final String	START_WEDGE2E		= "START_WEDGE2E";
	public static final String	END_S2SEDGE			= "END_S2SEDGE";
	public static final String	END_N2NEDGE			= "END_N2NEDGE";
	public static final String	END_W2WEDGE			= "END_W2WEDGE";
	public static final String	END_E2EEDGE			= "END_E2EEDGE";
	public static final String	STRAIGHT_ROAD_N2N	= "STRAIGHT_ROAD_N2N";
	public static final String	STRAIGHT_ROAD_S2S	= "STRAIGHT_ROAD_S2S";
	public static final String	STRAIGHT_ROAD_E2E	= "STRAIGHT_ROAD_E2E";
	public static final String	STRAIGHT_ROAD_W2W	= "STRAIGHT_ROAD_W2W";
	public static final String	CORNER_ROAD_W2S		= "CORNER_ROAD_W2S";
	public static final String	CORNER_ROAD_N2E		= "CORNER_ROAD_N2E";
	public static final String	CORNER_ROAD_E2S		= "CORNER_ROAD_E2S";
	public static final String	CORNER_ROAD_N2W		= "CORNER_ROAD_N2W";
	public static final String	CORNER_ROAD_S2W		= "CORNER_ROAD_S2W";
	public static final String	CORNER_ROAD_E2N		= "CORNER_ROAD_E2N";
	public static final String	CORNER_ROAD_S2E		= "CORNER_ROAD_S2E";
	public static final String	CORNER_ROAD_W2N		= "CORNER_ROAD_W2N";
	public static final String	INTER_TURN_N2EN		= "INTER_TURN_N2EN";
	public static final String	INTER_TURN_W2NW		= "INTER_TURN_W2NW";
	public static final String	INTER_TURN_S2WS		= "INTER_TURN_S2WS";
	public static final String	INTER_TURN_E2SE		= "INTER_TURN_E2SE";

	/* Straight roads in intersections */
	public static final String	INTER_TURN_S2ES_STR	= "INTER_TURN_E2SE_STR";
	public static final String	INTER_TURN_W2SW_STR	= "INTER_TURN_S2WS_STR";
	public static final String	INTER_TURN_N2WN_STR	= "INTER_TURN_W2NW_STR";
	public static final String	INTER_TURN_E2NE_STR	= "INTER_TURN_N2EN_STR";

	public static final String	NON_ROAD			= "NON_ROAD";

	/** Size of each grid in pixels */
	public static final int		DEFAULT_GRID_SIZE	= 30;
	/* Can be changed when there is not enough screen height */
	/** Size of each grid in pixels */
	public static int			GRID_SIZE			= 30;

	private String				type;
	private int					x;
	private int					y;
	private int					border;
	private BufferedImage		image;

	public MapGrid(String type, int x, int y)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.border = MapGridGroup.NIL;

		setSize(new Dimension(GRID_SIZE, GRID_SIZE));
		setPreferredSize(new Dimension(GRID_SIZE, GRID_SIZE));
		setMaximumSize(new Dimension(GRID_SIZE, GRID_SIZE));
		setMinimumSize(new Dimension(GRID_SIZE, GRID_SIZE));

		try
		{
			this.image = ImageIO.read(new File("images/" + type + ".png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Adds the image of this grid type to this JPanel. */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D) g;

		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();

		// How big is the image compared to the grid size?
		double sx = (GRID_SIZE / imageWidth);
		double sy = (GRID_SIZE / imageHeight);

		// Scale
		AffineTransform af = AffineTransform.getScaleInstance(sx, sy);
		g2D.drawImage(image, af, this);
	}

	public int getGridX()
	{
		return x;
	}

	public int getGridY()
	{
		return y;
	}

	public String getType()
	{
		return type;
	}

	public int getBorderType()
	{
		return border;
	}

	public BufferedImage getBufferedImage()
	{
		return image;
	}

	/** Returns the size of the original image. Since the image is a square and
	 * the width and height are the same length, only the width is returned.
	 * 
	 * @return The size of the original image. */
	public int getOriginalImageSize()
	{
		return image.getWidth();
	}
}
