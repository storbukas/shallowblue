package no.uib.inf112.group4.framework.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import no.uib.inf112.group4.exceptions.AbortedMoveException;
import no.uib.inf112.group4.exceptions.GameKilledException;
import no.uib.inf112.group4.framework.BoardAnalyzer;
import no.uib.inf112.group4.framework.BoardEvaluator;
import no.uib.inf112.group4.framework.Coordinate;
import no.uib.inf112.group4.framework.Game;
import no.uib.inf112.group4.framework.GameThread;
import no.uib.inf112.group4.framework.MiniMax;
import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.framework.PlayerMoveResponse;
import no.uib.inf112.group4.framework.PlayerType;
import no.uib.inf112.group4.framework.players.NetworkPlayer;
import no.uib.inf112.group4.interfaces.IGame;
import no.uib.inf112.group4.interfaces.IMove;
import no.uib.inf112.group4.interfaces.IPiece;
import no.uib.inf112.group4.interfaces.IPlayer;
import no.uib.inf112.group4.interfaces.IUserInterface;
import no.uib.inf112.group4.pieces.Piece;
import no.uib.inf112.group4.pieces.PieceType;

/**
 * The GUI is responsible for starting a game, visualizing the board, accepting
 * input from user and providing information about the state of the game
 * 
 */
public class GUI extends JFrame implements IUserInterface, ActionListener {
	// Generated serial ID
	private static final long serialVersionUID = -1322595100280365948L;

	static Coordinate location, destinaion;

	static final String splashImage = "res" + File.separator + "graphic"
			+ File.separator + "ShallowBlue_BlueCode.png";
	static final Color splashBackground = Color.white;
	static final long splashDuration = 3000; // ms

	static final int minimumSidebarWidth = 100;
	static final int minimumBoardLength = 400;
	static final Dimension minimumSize = new Dimension(minimumBoardLength + 2
			* minimumSidebarWidth, minimumBoardLength);

	// Following variables can be used by classes in same package
	BoardCanvas canvas = new BoardCanvas(this);
	IGame game = null;
	GameThread gamethread;
	JPanel rpanel, lpanel, cpanel, btnpanel;
	JLabel status = new JLabel("Game not started");
	AlgebraicListBox listAlgebraic = new AlgebraicListBox(this);
	JTabbedPane tabs = new JTabbedPane();
	NGPanel ngpanel = new NGPanel(this);
	OPanel opanel = new OPanel(this);
	CapturedPiecesViewer[] cappedPanel = new CapturedPiecesViewer[] {
			new CapturedPiecesViewer(this, PlayerColor.WHITE),
			new CapturedPiecesViewer(this, PlayerColor.BLACK),
	};

	JButton btnFile = new JButton("File");
	JMenuItem btnNewGame = new JMenuItem("New game");
	JMenuItem btnSaveGame = new JMenuItem("Save game");
	JMenuItem btnLoadGame = new JMenuItem("Load game");

	JButton btnGame = new JButton("Game");
	JMenuItem btnUndo = new JMenuItem("Undo move");
	JMenuItem btnRedo = new JMenuItem("Redo move");
	JMenuItem btnHint = new JMenuItem("Hint");
	JMenuItem btnDraw = new JMenuItem("Request draw");
	JMenuItem btnResign = new JMenuItem("Resign game");

	JButton btnView = new JButton("View");
	JMenuItem btnToggleLabels = new JMenuItem("Toggle labels");
	JMenuItem btnTogglePerspective = new JMenuItem("Toggle perspective");
	JMenuItem btnToggleAN = new JMenuItem("Toggle algorithmic view");
	JMenuItem btnToggleAIPlan = new JMenuItem("Toggle AI plan");
	JMenuItem btnToggleCapturedView = new JMenuItem("Hide captured pieces");

	JPopupMenu pmGame = new JPopupMenu();
	JPopupMenu pmView = new JPopupMenu();
	JPopupMenu pmFile = new JPopupMenu();

	private JButton[] buttonList = new JButton[] { btnFile, btnGame, btnView, };

	private JMenuItem[] menuItemList = new JMenuItem[] { btnNewGame,
			btnSaveGame, btnLoadGame, btnUndo, btnRedo, btnHint, btnDraw,
			btnToggleLabels, btnTogglePerspective, btnToggleAN, btnToggleAIPlan, 
			btnToggleCapturedView, btnResign };

	private JPanel overlayDrawer = null;
	private long overlayTimeout = 0;

	public boolean drawhint;

	/**
	 * Initializes GUI with given window size
	 */
	public GUI(int windowWidth, int windowHeight) {
		// Setup window
		super("Shallow Blue");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(windowWidth, windowHeight);
		setMinimumSize(minimumSize);
		
		tabs.add("local", ngpanel);
		tabs.add("online", opanel);
		init();


		// Set icon
		Image image = new ImageIcon(
				"/home/virtualgeek/Pictures/shallowblue-logo.png").getImage();
		setIconImage(image);

		setLocationRelativeTo(null);
		System.out.println("Showing GUI");
		setVisible(true);
	}

	/**
	 * A hand tailored layout manager for perfect placing of GUI components
	 */
	private LayoutManager layoutmngr = new LayoutManager() {
		@Override
		public void addLayoutComponent(String arg0, Component arg1) {
			// No need
		}

		@Override
		public void layoutContainer(Container c) {
			int boardlength = Math.min(c.getWidth() - 2 * minimumSidebarWidth,
					c.getHeight());
			int sidebarwidth = (c.getWidth() - boardlength) / 2;
			cpanel.setBounds(sidebarwidth, 0, boardlength, c.getHeight());
			rpanel.setBounds(sidebarwidth + boardlength, 0, sidebarwidth,
					c.getHeight());
			lpanel.setBounds(0, 0, sidebarwidth, c.getHeight());
			for (JButton btn : buttonList) {
				btn.setMaximumSize(new Dimension(100, 25));
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return minimumSize;
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return null;
		}

		@Override
		public void removeLayoutComponent(Component arg0) {
		}

	};

	/**
	 * Adds components, listeners and everything needed for it to look pretty
	 */
	private void init() {
		// Create popup menus
		for (JMenuItem mi : menuItemList) {
			mi.addActionListener(this);
		}
		pmFile.add(btnNewGame);
		pmFile.add(btnSaveGame);
		pmFile.add(btnLoadGame);
		pmGame.add(btnUndo);
		pmGame.add(btnRedo);
		pmGame.add(btnHint);
		pmGame.add(btnDraw);
		pmGame.add(btnResign);
		pmView.add(btnToggleLabels);
		pmView.add(btnTogglePerspective);
		pmView.add(btnToggleAN);
		pmView.add(btnToggleAIPlan);
		pmView.add(btnToggleCapturedView);

		// Create status frames
		StatusFrame[] frames = { new StatusFrame(this, true),
				new StatusFrame(this, false) };

		// Create button panel
		btnpanel = new JPanel();
		btnpanel.setBackground(BoardCanvas.backgroundColor);
		btnpanel.setLayout(new BoxLayout(btnpanel, BoxLayout.PAGE_AXIS));
		btnpanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		for (JButton btn : buttonList) {
			btn.setAlignmentX(Component.RIGHT_ALIGNMENT);
			btn.addActionListener(this);
			btnpanel.add(btn);
		}

		// Create left middle panel
		JPanel lmpanel = new JPanel();
		lmpanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 10));
		lmpanel.add(cappedPanel[0]);
		lmpanel.setBackground(BoardCanvas.backgroundColor);

		// Create left panel
		lpanel = new JPanel();
		lpanel.setLayout(new BorderLayout());
		lpanel.setBackground(BoardCanvas.backgroundColor);
		lpanel.setPreferredSize(new Dimension(150, 0));
		lpanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lpanel.add("North", frames[0]);
		lpanel.add("South", btnpanel);
		lpanel.add("Center", lmpanel);

		// Create right middle panel
		JPanel rmpanel = new JPanel();
		rmpanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 10));
		rmpanel.add("North", cappedPanel[1]);
		rmpanel.setBackground(BoardCanvas.backgroundColor);

		// Create right panel
		rpanel = new JPanel();
		rpanel.setLayout(new BorderLayout());
		rpanel.setBackground(BoardCanvas.backgroundColor);
		rpanel.setPreferredSize(new Dimension(150, 0));
		rpanel.add("North", frames[1]);
		rpanel.add("South", listAlgebraic);
		rpanel.add("Center", rmpanel);
		rpanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Create status panel
		JPanel statuspanel = new JPanel();
		status.setAlignmentX(Component.CENTER_ALIGNMENT);
		statuspanel.setBackground(BoardCanvas.backgroundColor);
		statuspanel.add(status);
		// Create center panel
		cpanel = new JPanel();
		cpanel.setLayout(new BorderLayout());
		cpanel.setBackground(BoardCanvas.backgroundColor);
		cpanel.add("Center", canvas);
		cpanel.add("North", statuspanel);
		// add components
		setLayout(layoutmngr);
		add("West", lpanel);
		add("East", rpanel);
		add("Center", cpanel);
		BufferedImage splash = null;
		try {
			splash = ImageIO.read(new File(splashImage));
		} catch (IOException e) {
			System.out.println("Failed to load splash screen: " + splashImage);
		}
		if (splash != null) {
			System.out.println("Showing splash");
			showSplash(splash, this);
		}
	}

	@Override
	public void paint(Graphics g) {
		if (overlayDrawer != null
				&& System.currentTimeMillis() >= overlayTimeout) {
			getLayeredPane().remove(overlayDrawer);
			overlayDrawer = null;
		}
		if (game != null) {
			if (game.isChecked(game.getActivePlayer())) {
				statusCheck();
			} else {
				setStatus("It is " + game.getActivePlayer().getName()
						+ "'s turn");
			}
		}
		/* Drawing on an independent image
		 * to avoid flickering
		 */
		Image img = createImage(g.getClipBounds().width,
				g.getClipBounds().height);
		super.paint(img.getGraphics());
		g.drawImage(img, 0, 0, this);
	}
	boolean firstStart = true;

	private IMove hintMove;



	public void newGame(){
		int result = JOptionPane.showInternalConfirmDialog(
				getContentPane(), tabs, "Start new game",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(tabs.getSelectedComponent().equals(ngpanel)){
			if (result != JOptionPane.OK_OPTION)
				return;

			game = new Game(ngpanel.getWhite(), ngpanel.getBlack(), this);
			start(game);
		}
		if(tabs.getSelectedComponent().equals(opanel)){
			if (result != JOptionPane.OK_OPTION)
				return;
			
			Object[] options = {"cancel"};
			int r = JOptionPane.showOptionDialog(this.getContentPane(),
		    		"waiting for other player...", "Network",
		                   JOptionPane.PLAIN_MESSAGE,
		                   JOptionPane.QUESTION_MESSAGE,
		                   null,
		                   options,
		                   options[0]);

			NetworkPlayer player = opanel.getPlayer();
			if(player.isServer()){
		    
			
			while(!player.net.getConnected() || r == JOptionPane.CANCEL_OPTION){
				
			}
			if(r == JOptionPane.CANCEL_OPTION){
				return;
			}
			else{
				JOptionPane.showMessageDialog(null, "Loading Complete...!!!");
			}
			}
			else{
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(player.net.getConnected())
					JOptionPane.showMessageDialog(null, "Connected to host!!!!");
				else
					return;
			}
				
		}
	}
	/**
	 * Starts a new game using the given game implementation
	 */
	@Override
	public void start(IGame game) {
		this.game = game;
		if (firstStart) {
			// Allow splash screen to disappear
			while (overlayDrawer != null) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			firstStart = false;
			newGame();
			return;
		}
		if (overlayDrawer != null)
			getLayeredPane().remove(overlayDrawer);
		overlayDrawer = null;
		System.out.println("GUI: Starting new game");
		game.startClock(!ngpanel.isFIDE());
		if (gamethread != null && gamethread.isAlive()) {
			// Important that we change gamethread to null before we call
			// Thread.interrupt().
			// The thread will only be destroyed if GUI.gamethread != The
			// interrupted thread.
			// See: promptMove().
			Thread oldThread = gamethread;
			gamethread = null;
			oldThread.interrupt();
		}
		gamethread = new GameThread(game);
		gamethread.start();
		// Disable/enable undo/redo
		if(game.getPlayer(PlayerColor.WHITE).getPlayerType() == PlayerType.NETWORK
			|| game.getPlayer(PlayerColor.BLACK).getPlayerType() == PlayerType.NETWORK) {
			btnUndo.setEnabled(false);
			btnRedo.setEnabled(false);
		} else {
			btnUndo.setEnabled(true);
			btnRedo.setEnabled(true);
		}
	}

	@Override
	public PlayerMoveResponse promptMove(IPlayer player) throws AbortedMoveException {
		try {
			return canvas.promptMove(player);
		} catch (InterruptedException e) {
			if (gamethread != Thread.currentThread()) {
				// Kill thread
				System.out.println("GUI: Killed old game thread.");
				throw new GameKilledException();
			} else {
				// Abort move
				System.out.println("GUI: Aborting move.");
				throw new AbortedMoveException();
			}
		}
	}

	@Override
	public void update() {
		BoardAnalyzer ba = game.getBoard().getAnalyzer();
		if(ba.canClaimDraw()) {
			btnDraw.setText("Claim draw");
		} else {
			btnDraw.setText("Request draw");
		}
		this.repaint();
	}

	static private Font overlayFont = new Font("TimerRoman", Font.BOLD, 50);

	@Override
	public void showGameOver(final IPlayer player) {
		setStatus("Game over!");
		if (game.isDraw()) {
		} else {
			Sounds.playCheckMate();
		}
		@SuppressWarnings("serial")
		JPanel gameoverDrawer = new JPanel() {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setFont(overlayFont);
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				// Draw "Game over!"
				String s = "Game over!";
				Rectangle2D textsize = g2.getFontMetrics().getStringBounds(s,
						g2);
				drawText(g2, s, getWidth() / 2 - (int) textsize.getWidth() / 2,
						getHeight() / 2 - (int) textsize.getHeight() / 2);
				// Draw victor
				if (player != null) {
					s = player.getName() + " won!";
				} else if (game.isStalemate()) {
					s = "Stalemate!";
				} else {
					s = "Draw!";
				}
				
				textsize = g2.getFontMetrics().getStringBounds(s, g2);
				drawText(g2, s, getWidth() / 2 - (int) textsize.getWidth() / 2,
						getHeight() / 2 + (int) textsize.getHeight() / 2);
			}

			// Draws text with outline
			public void drawText(Graphics2D g2, String s, int x, int y) {
				g2.setColor(Color.DARK_GRAY);
				for (int i = -1; i < 2; i += 2)
					for (int j = -1; j < 2; j += 2) {
						g2.drawString(s, x + i * 2, y + j * 2);
					}
				g2.setColor(Color.RED);
				g2.drawString(s, x, y);
			}
		};
		gameoverDrawer.setOpaque(false);
		gameoverDrawer.setBounds(cpanel.getBounds());
		setOverlay(gameoverDrawer, 1000000);
	}

	private void showSplash(final BufferedImage img, final GUI gui) {
		@SuppressWarnings("serial")
		JPanel splash = new JPanel() {
			@Override
			public void paint(Graphics g) {
				// Draws splash image centered on screen scaled with
				// aspect ratio to fit width or height
				Point pos = gui.getContentPane().getLocationOnScreen();
				Rectangle clientRect = gui.getContentPane().getBounds();
				clientRect.x = pos.x;
				clientRect.y = pos.y;
				float ratio = Math.min(
						(float) clientRect.getWidth() / img.getWidth(),
						(float) clientRect.getHeight() / img.getHeight());
				g.setColor(splashBackground);
				g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
				Image scaled = img.getScaledInstance((int) (img.getWidth()
						* ratio - 20), (int) (img.getHeight() * ratio),
						Image.SCALE_SMOOTH);
				g.drawImage(
						scaled,
						(int) clientRect.getWidth() / 2 - scaled.getWidth(gui)
						/ 2,
						(int) clientRect.getHeight() / 2
						- scaled.getHeight(gui) / 2, gui);
			}
		};
		setOverlay(splash, splashDuration);
	}

	@Override
	public void statusCheck() {
		setStatus("Check");
	}

	@Override
	public void showInvalidMoveMessage(IMove move, String message) {
		System.out.println(message);
		setStatus("Invalid move!");
	}

	private void setOverlay(JPanel comp, long ms) {
		overlayDrawer = comp;
		getLayeredPane().add(comp);
		comp.setLocation(0, 0);
		comp.setSize(getSize());
		overlayTimeout = System.currentTimeMillis() + ms;
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				repaint();
			}
		}, ms);
		repaint();
		System.out.println("Set overlay");
	}

	public void setStatus(String st) {
		status.setText(st);
		//repaint(); // Causes a recursion loop!
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFile) {
			pmFile.show((Component) e.getSource(), btnFile.getWidth(), 0);
		} else if (e.getSource() == btnGame) {
			pmGame.show((Component) e.getSource(), btnGame.getWidth(), 0);
		} else if (e.getSource() == btnView) {
			pmView.show((Component) e.getSource(), btnView.getWidth(), 0);
		} else if (e.getSource() == btnNewGame) {
			newGame();
		} else if (e.getSource() == btnHint) {
			hintMove = MiniMax.evaluate(game.getBoard(), new BoardEvaluator(), 4).move;
			canvas.setHint(hintMove);
			repaint();
		} 
		else if (e.getSource() == btnUndo) {
			undo();
		} else if (e.getSource() == btnRedo) {
			redo();
		} else if (e.getSource() == btnToggleAN) {
			listAlgebraic.setVisible(!listAlgebraic.isVisible());
		} else if (e.getSource() == btnToggleAIPlan) {
			canvas.toggleAIPlan();
		} else if (e.getSource() == btnToggleLabels) {
			canvas.toggleLabels();
		} else if (e.getSource() == btnTogglePerspective) {
			canvas.togglePerspective();
		} else if (e.getSource() == btnLoadGame) {
			// TODO: Implement this
		} else if (e.getSource() == btnSaveGame) {
			// TODO: Implement this
		} else if (e.getSource() == btnDraw) {
			BoardAnalyzer ba = game.getBoard().getAnalyzer();
			if(ba.canClaimDraw()) {
				// claim draw
				if(game.claimDraw()) {
					canvas.hasPrompt = false;
					game.completeAbortMove();
				}
			} else {
				// request draw
			}
			update();
		} else if (e.getSource() == btnToggleCapturedView) {
			toggleCapturedView();
		} else if(e.getSource() == btnResign) {
			System.out.println("Resigning");
			IPlayer resignee = game.getActivePlayer().getPlayerType()
					== PlayerType.LOCAL ? game.getActivePlayer() : game.getInactivePlayer();
			if(game.resign(resignee)) {
				canvas.hasPrompt = false;
				game.completeAbortMove();
			}
			update();
		}
	}

	private void undo() {
		if (hasHumanPlayer(game.getPlayers()) == false) {
			return;
		}

		game.startAbortMove();
		gamethread.interrupt();
		game.undo();
		PlayerType playerType = game.getActivePlayer().getPlayerType();
		if (playerType != PlayerType.LOCAL) {
			game.undo();
		}
		game.completeAbortMove();
		repaint();
	}

	private void redo() {
		if (hasHumanPlayer(game.getPlayers()) == false) {
			return;
		}

		game.startAbortMove();
		gamethread.interrupt();
		game.redo();
		PlayerType playerType = game.getActivePlayer().getPlayerType();
		if (playerType != PlayerType.LOCAL) {
			game.redo();
		}
		game.completeAbortMove();
		repaint();
	}

	private void toggleCapturedView() {
		btnToggleCapturedView.setText((cappedPanel[0].isVisible()?"Show":"Hide") + " captured pieces");
		cappedPanel[0].setVisible(!cappedPanel[0].isVisible());
		cappedPanel[1].setVisible(!cappedPanel[1].isVisible());
		repaint();
	}

	private boolean hasHumanPlayer(IPlayer[] players) {
		PlayerType p1 = players[0].getPlayerType();
		PlayerType p2 = players[1].getPlayerType();
		return p1 != PlayerType.COMPUTER || p2 != PlayerType.COMPUTER;
	}

	@Override
	public IPiece promptPromotionPiece(PlayerColor color) {
		PieceType pt = PromotionFrame.getPromotedPiece(this,  color);
		Sounds.playPromotion();
		return Piece.create(pt, color);
	}

	@Override
	public void moveCompleted() {
		Sounds.playMovePiece();
		hintMove = null;
	}

}
