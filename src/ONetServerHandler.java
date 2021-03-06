import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

public class ONetServerHandler extends ONetHandler implements OICommandListener {
    public static Logger    a = Logger.getLogger("Minecraft");
    public ONetworkManager  b;
    public boolean          c = false;
    private MinecraftServer d;
    private OEntityPlayerMP e;
    private int             f;
    private int             g;
    private boolean         h;
    private double          i;
    private double          j;
    private double          k;
    private boolean         l = true;

    private Map             m = new HashMap();

    public ONetServerHandler(MinecraftServer paramMinecraftServer, ONetworkManager paramONetworkManager, OEntityPlayerMP paramOEntityPlayerMP) {
        d = paramMinecraftServer;
        b = paramONetworkManager;
        paramONetworkManager.a(this);
        e = paramOEntityPlayerMP;
        paramOEntityPlayerMP.a = this;
    }

    /**
     * Returns the item in player's hand
     * 
     * @return
     */
    public int getItemInHand() {
        if (e.i.b() != null)
            return e.i.b().c;
        return -1;
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    /**
     * Sends a message to the player
     * 
     * @param msg
     */
    public void msg(String msg) {
        b(new OPacket3Chat(msg));
    }

    
    public void a() {
        h = false;
        b.a();
        if (f - g > 20)
            b(new OPacket0KeepAlive());
    }

    public void a(String paramString) {
        b(new OPacket255KickDisconnect(paramString));
        b.c();
        d.f.a(new OPacket3Chat("§e" + e.r + " left the game."));
        d.f.c(e);
        c = true;
    }

    @Override
    public void a(OPacket27 paramOPacket27) {
        e.a(paramOPacket27.c(), paramOPacket27.e(), paramOPacket27.g(), paramOPacket27.h(), paramOPacket27.d(), paramOPacket27.f());
    }

    @Override
    public void a(OPacket10Flying paramOPacket10Flying) {
        h = true;
        double d1;
        if (!l) {
            d1 = paramOPacket10Flying.b - j;
            if ((paramOPacket10Flying.a == i) && (d1 * d1 < 0.01D) && (paramOPacket10Flying.c == k))
                l = true;
        }
        // CanaryMod: Notice player movement
        if (etc.floor(i) != etc.floor(getPlayer().getX()) || etc.floor(j) != etc.floor(getPlayer().getY()) || etc.floor(k) != etc.floor(getPlayer().getZ())) {
            Location from = new Location();
            from.x = etc.floor(i);
            from.y = etc.floor(j);
            from.z = etc.floor(k);
            from.rotX = getPlayer().getRotation();
            from.rotY = getPlayer().getPitch();

            Location to = new Location();
            to.x = etc.floor(e.aK);
            to.y = etc.floor(e.aL);
            to.z = etc.floor(e.aM);
            to.rotX = getPlayer().getRotation();
            to.rotY = getPlayer().getPitch();

            OEntity.manager.callHook(PluginLoader.Hook.PLAYER_MOVE, getPlayer(), from, to);
        }

        if (l) {
            if (e.aF != null) {
                float f1 = e.aQ;
                float f2 = e.aR;
                e.aF.h_();
                double d2 = e.aK;
                double d3 = e.aL;
                double d4 = e.aM;
                double d5 = 0.0D;
                double d6 = 0.0D;
                if (paramOPacket10Flying.i) {
                    f1 = paramOPacket10Flying.e;
                    f2 = paramOPacket10Flying.f;
                }
                if ((paramOPacket10Flying.h) && (paramOPacket10Flying.b == -999.0D) && (paramOPacket10Flying.d == -999.0D)) {
                    d5 = paramOPacket10Flying.a;
                    d6 = paramOPacket10Flying.c;
                }

                e.aV = paramOPacket10Flying.g;

                e.a(true);
                e.c(d5, 0.0D, d6);
                e.b(d2, d3, d4, f1, f2);
                e.aN = d5;
                e.aP = d6;
                if (e.aF != null)
                    d.e.b(e.aF, true);
                if (e.aF != null)
                    e.aF.h_();
                d.f.b(e);
                i = e.aK;
                j = e.aL;
                k = e.aM;
                d.e.f(e);

                return;
            }

            d1 = e.aL;
            i = e.aK;
            j = e.aL;
            k = e.aM;

            double d2 = e.aK;
            double d3 = e.aL;
            double d4 = e.aM;

            float f3 = e.aQ;
            float f4 = e.aR;

            if ((paramOPacket10Flying.h) && (paramOPacket10Flying.b == -999.0D) && (paramOPacket10Flying.d == -999.0D))
                paramOPacket10Flying.h = false;

            if (paramOPacket10Flying.h) {
                d2 = paramOPacket10Flying.a;
                d3 = paramOPacket10Flying.b;
                d4 = paramOPacket10Flying.c;
                double d6 = paramOPacket10Flying.d - paramOPacket10Flying.b;
                if ((d6 > 1.65D) || (d6 < 0.1D)) {
                    a("Illegal stance");
                    a.warning(e.r + " had an illegal stance: " + d6);
                }
            }
            if (paramOPacket10Flying.i) {
                f3 = paramOPacket10Flying.e;
                f4 = paramOPacket10Flying.f;
            }

            e.a(true);
            e.bl = 0.0F;
            e.b(i, j, k, f3, f4);

            double d6 = d2 - e.aK;
            double d7 = d3 - e.aL;
            double d8 = d4 - e.aM;

            float f5 = 0.0625F;
            int n = d.e.a(e, e.aU.b().e(f5, f5, f5)).size() == 0 ? 1 : 0;

            e.c(d6, d7, d8);
            d6 = d2 - e.aK;
            d7 = d3 - e.aL;
            if ((d7 > -0.5D) || (d7 < 0.5D))
                d7 = 0.0D;
            d8 = d4 - e.aM;
            double d9 = d6 * d6 + d7 * d7 + d8 * d8;
            int i1 = 0;
            if ((d9 > 0.0625D) && (!e.F())) {
                i1 = 1;
                a.warning(e.r + " moved wrongly!");
                System.out.println("Got position " + d2 + ", " + d3 + ", " + d4);
                System.out.println("Expected " + e.aK + ", " + e.aL + ", " + e.aM);
            }
            e.b(d2, d3, d4, f3, f4);

            int i2 = d.e.a(e, e.aU.b().e(f5, f5, f5)).size() == 0 ? 1 : 0;
            if ((n != 0) && ((i1 != 0) || (i2 == 0)) && (!e.F())) {
                a(i, j, k, f3, f4);
                return;
            }

            e.aV = paramOPacket10Flying.g;
            d.f.b(e);
            e.b(e.aL - d1, paramOPacket10Flying.g);
        }
    }

    public void a(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
        // CanaryMod: Teleportation hook
        Location from = new Location();
        from.x = paramDouble1;
        from.y = paramDouble2;
        from.z = paramDouble3;
        from.rotX = paramFloat1;
        from.rotY = paramFloat2;
        Player player = getPlayer();
        if ((Boolean) OEntity.manager.callHook(PluginLoader.Hook.TELEPORT, player, player.getLocation(), from))
            return;

        
        l = false;
        i = paramDouble1;
        j = paramDouble2;
        k = paramDouble3;
        e.b(paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
        e.a.b(new OPacket13PlayerLookMove(paramDouble1, paramDouble2 + 1.620000004768372D, paramDouble2, paramDouble3, paramFloat1, paramFloat2, false));
    }

    // CanaryMod: Store x/y/z
    int x, y, z, type;

    @Override
    public void a(OPacket14BlockDig paramOPacket14BlockDig) {
        if (paramOPacket14BlockDig.e == 4) {
            e.z();
            return;
        }
        // CanaryMod: We allow admins and ops to dig!
        boolean bool = d.e.v = d.f.h(e.r) || getPlayer().isAdmin();
        int n = 0;
        if (paramOPacket14BlockDig.e == 0)
            n = 1;

        if (paramOPacket14BlockDig.e == 2)
            n = 1;

        int i1 = paramOPacket14BlockDig.a;
        int i2 = paramOPacket14BlockDig.b;
        int i3 = paramOPacket14BlockDig.c;
        if (n != 0) {
            double d1 = e.aK - (i1 + 0.5D);
            double d2 = e.aL - (i2 + 0.5D);
            double d3 = e.aM - (i3 + 0.5D);
            double d4 = d1 * d1 + d2 * d2 + d3 * d3;
            if (d4 > 36.0D)
                return;
        }
        OChunkCoordinates localOChunkCoordinates = d.e.m();
        int i4 = (int) OMathHelper.e(i1 - localOChunkCoordinates.a);
        int i5 = (int) OMathHelper.e(i3 - localOChunkCoordinates.c);
        if (i4 > i5)
            i5 = i4;
        // CanaryMod: the player
        Player player = getPlayer();

        if (paramOPacket14BlockDig.e == 0) {
            // CanaryMod: Start digging
            // No buildrights
            if (!getPlayer().canBuild())
                return;
            // CanaryMod: Custom spawn prot size
            if ((i5 > etc.getInstance().getSpawnProtectionSize()) || (bool)) {
                // CanaryMod: Dig hooks
                Block block = etc.getServer().getBlockAt(i1, i2, i3);
                block.setStatus(0); // Started digging
                x = block.getX();
                y = block.getY();
                z = block.getZ();
                type = block.getType();
                if (!(Boolean) OEntity.manager.callHook(PluginLoader.Hook.BLOCK_DESTROYED, player, block))
                    e.c.a(i1, i2, i3);
            }

        } else if (paramOPacket14BlockDig.e == 2)
            e.c.b(i1, i2, i3);
        else if (paramOPacket14BlockDig.e == 3) {
            // CanaryMod: Break block
            Block block = new Block(type, x, y, z);
            block.setStatus(3);
            OEntity.manager.callHook(PluginLoader.Hook.BLOCK_DESTROYED, player, block);

            double d5 = e.aK - (i1 + 0.5D);
            double d6 = e.aL - (i2 + 0.5D);
            double d7 = e.aM - (i3 + 0.5D);
            double d8 = d5 * d5 + d6 * d6 + d7 * d7;
            if (d8 < 256.0D)
                e.a.b(new OPacket53BlockChange(i1, i2, i3, d.e));
        }
        d.e.v = false;
    }

    // CanaryMod: Store the blocks between blockPlaced packets
    Block lastRightClicked;

    
    @Override
    public void a(OPacket15Place paramOPacket15Place) {
        OItemStack localOItemStack = e.i.b();

        // CanaryMod: Store block data to call hooks
        // CanaryMod START
        Block blockClicked = null;
        Block blockPlaced = null;

        // We allow admins and ops to build!
        boolean bool = d.e.v = d.f.h(e.r) || getPlayer().isAdmin();
        if (paramOPacket15Place.d == 255) {
            // ITEM_USE -- if we have a lastRightClicked then it could be a
            // usable location
            blockClicked = lastRightClicked;
            lastRightClicked = null;
        } else {
            // RIGHTCLICK or BLOCK_PLACE .. or nothing
            blockClicked = new Block(etc.getServer().getBlockIdAt(paramOPacket15Place.a, paramOPacket15Place.b, paramOPacket15Place.c), paramOPacket15Place.a, paramOPacket15Place.b, paramOPacket15Place.c);
            blockClicked.setFaceClicked(Block.Face.fromId(paramOPacket15Place.d));
            lastRightClicked = blockClicked;
        }

     // If we clicked on something then we also have a location to place the
     // block
     if (blockClicked != null && localOItemStack != null) {
         blockPlaced = new Block(localOItemStack.c, blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
         switch (paramOPacket15Place.d) {
             case 0:
                 blockPlaced.setY(blockPlaced.getY() - 1);
                 break;
             case 1:
                 blockPlaced.setY(blockPlaced.getY() + 1);
                 break;
             case 2:
                 blockPlaced.setZ(blockPlaced.getZ() - 1);
                 break;
             case 3:
                 blockPlaced.setZ(blockPlaced.getZ() + 1);
                 break;
             case 4:
                 blockPlaced.setX(blockPlaced.getX() - 1);
                 break;
             case 5:
                 blockPlaced.setX(blockPlaced.getX() + 1);
                 break;
         }
     }
     // CanaryMod: END

        if (paramOPacket15Place.d == 255) {
            // CanaryMod: call our version with extra blockClicked/blockPlaced
            if (blockPlaced != null)
                // Set the type of block to what it currently is
                blockPlaced.setType(etc.getServer().getBlockIdAt(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ()));

            if (localOItemStack == null)
                return;
            ((Digging) e.c).a(e, d.e, localOItemStack, blockPlaced, blockClicked);
        } else {
            int n = paramOPacket15Place.a;
            int i1 = paramOPacket15Place.b;
            int i2 = paramOPacket15Place.c;
            int i3 = paramOPacket15Place.d;
            OChunkCoordinates localOChunkCoordinates = d.e.m();
            // CanaryMod : Fix stupid buggy spawn protection.
            int i4 = (int) OMathHelper.e((i3 == 4 ? n - 1 : (i3 == 5 ? (n + 1) : n)) - localOChunkCoordinates.a);
            // CanaryMod : Fix stupid buggy spawn protection.
            int i5 = (int) OMathHelper.e((i3 == 2 ? i2 - 1 : (i3 == 3 ? (i2 + 1) : i2)) - localOChunkCoordinates.c);

            if (i4 > i5)
                i5 = i4;
            // CanaryMod: call BLOCK_RIGHTCLICKED
            Item item = (localOItemStack != null) ? new Item(localOItemStack) : new Item(Item.Type.Air);
            Player player = getPlayer();
            OEntity.manager.callHook(PluginLoader.Hook.BLOCK_RIGHTCLICKED, player, blockClicked, item);

            // CanaryMod: call original BLOCK_CREATED
            OEntity.manager.callHook(PluginLoader.Hook.BLOCK_CREATED, player, blockPlaced, blockClicked, item.getItemId());
            // CanaryMod: If we were building inside spawn, bail! (unless ops/admin)

            if (((i5 > etc.getInstance().getSpawnProtectionSize() && !etc.getInstance().isOnItemBlacklist(item.getItemId())) || bool) && player.canBuild())
                e.c.a(e, d.e, localOItemStack, n, i1, i2, i3);
            else {
                // CanaryMod: No point sending the client to update the blocks, you
                // weren't allowed to place!
                d.e.v = false;
                return;
            }

            // CanaryMod: these are the 'block changed' packets for the client.

            e.a.b(new OPacket53BlockChange(n, i1, i2, d.e));

            if (i3 == 0)
                i1--;
            if (i3 == 1)
                i1++;
            if (i3 == 2)
                i2--;
            if (i3 == 3)
                i2++;
            if (i3 == 4)
                n--;
            if (i3 == 5)
                n++;

            e.a.b(new OPacket53BlockChange(n, i1, i2, d.e));
        }
        if ((localOItemStack != null) && (localOItemStack.a == 0))
            e.i.a[e.i.c] = null;

        e.h = true;
        e.i.a[e.i.c] = OItemStack.b(e.i.a[e.i.c]);
        OSlot localOSlot = e.k.a(e.i, e.i.c);
        e.k.a();
        e.h = false;

        if (!OItemStack.a(e.i.b(), paramOPacket15Place.e))
            b(new OPacket103(e.k.f, localOSlot.a, e.i.b()));

        d.e.v = false;
    }

    @Override
    public void a(String paramString, Object[] paramArrayOfObject) {
        // CanaryMod: disconnect!
        OEntity.manager.callHook(PluginLoader.Hook.DISCONNECT, getPlayer());
        a.info(e.r + " lost connection: " + paramString);
        d.f.a(new OPacket3Chat("§e" + e.r + " left the game."));
        d.f.c(e);
        c = true;
    }

    @Override
    public void a(OPacket paramOPacket) {
        a.warning(getClass() + " wasn't prepared to deal with a " + paramOPacket.getClass());
        a("Protocol error, unexpected packet");
    }

    public void b(OPacket paramOPacket) {
        b.a(paramOPacket);
        g = f;
    }

    @Override
    public void a(OPacket16BlockItemSwitch paramOPacket16BlockItemSwitch) {
        if ((paramOPacket16BlockItemSwitch.a < 0) || (paramOPacket16BlockItemSwitch.a > OInventoryPlayer.e())) {
            a.warning(e.r + " tried to set an invalid carried item");
            return;
        }
        e.i.c = paramOPacket16BlockItemSwitch.a;
    }

    @Override
    public void a(OPacket3Chat paramOPacket3Chat) {
        String str = paramOPacket3Chat.a;
        // CanaryMod: redirect chathandling to player class
        getPlayer().chat(str);

    }

    private void c(String paramString) {

    }

    @Override
    public void a(OPacket18ArmAnimation paramOPacket18ArmAnimation) {
        if (paramOPacket18ArmAnimation.b == 1) {
            // CanaryMod: Swing the arm!
            OEntity.manager.callHook(PluginLoader.Hook.ARM_SWING, getPlayer());

            e.m_();
        }
    }

    @Override
    public void a(OPacket19 paramOPacket19) {
        if (paramOPacket19.b == 1)
            e.e(true);
        else if (paramOPacket19.b == 2)
            e.e(false);
        else if (paramOPacket19.b == 3) {
            e.a(false, true, true);
            l = false;
        }
    }

    @Override
    public void a(OPacket255KickDisconnect paramOPacket255KickDisconnect) {
        b.a("disconnect.quitting", new Object[0]);
    }

    public int b() {
        return b.d();
    }

    public void b(String paramString) {
        b(new OPacket3Chat("§7" + paramString));
    }

    public String c() {
        return e.r;
    }

    @Override
    public void a(OPacket7 paramOPacket7) {
        OEntity localOEntity = d.e.a(paramOPacket7.b);

        if ((localOEntity != null) && (e.e(localOEntity)) && (e.f(localOEntity) < 4.0F))
            if (paramOPacket7.c == 0)
                e.c(localOEntity);
            else if (paramOPacket7.c == 1)
                e.d(localOEntity);
    }

    @Override
    public void a(OPacket9 paramOPacket9) {
        if (e.W > 0)
            return;

        e = d.f.d(e);
    }

    @Override
    public void a(OPacket101 paramOPacket101) {
        e.w();
    }

    @Override
    public void a(OPacket102 paramOPacket102) {
        if ((e.k.f == paramOPacket102.a) && (e.k.c(e))) {
            OItemStack localOItemStack = e.k.a(paramOPacket102.b, paramOPacket102.c, e);

            if (OItemStack.a(paramOPacket102.e, localOItemStack)) {
                e.a.b(new OPacket106(paramOPacket102.a, paramOPacket102.d, true));
                e.h = true;
                e.k.a();
                e.v();
                e.h = false;
            } else {
                m.put(Integer.valueOf(e.k.f), Short.valueOf(paramOPacket102.d));
                e.a.b(new OPacket106(paramOPacket102.a, paramOPacket102.d, false));
                e.k.a(e, false);

                ArrayList localArrayList = new ArrayList();
                for (int n = 0; n < e.k.e.size(); n++)
                    localArrayList.add(((OSlot) e.k.e.get(n)).a());
                e.a(e.k, localArrayList);
            }
        }
    }

    @Override
    public void a(OPacket106 paramOPacket106) {
        Short localShort = (Short) m.get(Integer.valueOf(e.k.f));
        if ((localShort != null) && (paramOPacket106.b == localShort.shortValue()) && (e.k.f == paramOPacket106.a) && (!e.k.c(e)))
            e.k.a(e, true);
    }

    @Override
    public void a(OPacket130 paramOPacket130) {
        if (d.e.f(paramOPacket130.a, paramOPacket130.b, paramOPacket130.c)) {
            OTileEntity localOTileEntity = d.e.m(paramOPacket130.a, paramOPacket130.b, paramOPacket130.c);

            if ((localOTileEntity instanceof OTileEntitySign)) {
                OTileEntitySign localOTileEntitySign1 = (OTileEntitySign) localOTileEntity;
                if (!localOTileEntitySign1.a()) {
                    d.c("Player " + e.r + " just tried to change non-editable sign");
                    return;
                }
            }
            int i1;
            int i2;
            for (int n = 0; n < 4; n++) {
                i1 = 1;
                // CanaryMod: Remove the char limit, for plugins.
                // if (paramOPacket130.d[n].length() > 15) {
                // i1 = 0;
                // } else {

                    for (i2 = 0; i2 < paramOPacket130.d[n].length(); i2++)
                        if (OFontAllowedCharacters.a.indexOf(paramOPacket130.d[n].charAt(i2)) < 0)
                            i1 = 0;
                if (i1 == 0)
                    paramOPacket130.d[n] = "!?";
           }
            if ((localOTileEntity instanceof OTileEntitySign)) {
                int n = paramOPacket130.a;
                i1 = paramOPacket130.b;
                i2 = paramOPacket130.c;
                
                OTileEntitySign localOTileEntitySign2 = (OTileEntitySign) localOTileEntity;
                // CanaryMod: Copy the old line text
                String[] old = Arrays.copyOf(localOTileEntitySign2.a, localOTileEntitySign2.a.length);

                // CanaryMod: Check if we can change it
                Sign sign = new Sign(localOTileEntitySign2);
                if ((Boolean) OEntity.manager.callHook(PluginLoader.Hook.SIGN_CHANGE, getPlayer(), sign))
                    localOTileEntitySign2.a = Arrays.copyOf(old, old.length);

                
                for (int i3 = 0; i3 < 4; i3++)
                    localOTileEntitySign2.a[i3] = paramOPacket130.d[i3];
                localOTileEntitySign2.i();
                d.e.g(n, i1, i2);
            }
        }
    }
}
