package content.region.kandarin.seersvillage.quest.mcannon.plugin;

/**
 * The enum Dmc revolution.
 */
public enum DMCRevolution {

    /**
     * The North.
     */
    NORTH(515) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetY > 0 && Math.abs(offsetX) <= offsetY;
        }
    },

    /**
     * The North east.
     */
    NORTH_EAST(516) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetY > 0 && offsetX > 0;
        }
    },

    /**
     * The East.
     */
    EAST(517) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetX > 0 && Math.abs(offsetY) <= offsetX;
        }
    },

    /**
     * The South east.
     */
    SOUTH_EAST(518) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetY < 0 && offsetX > 0;
        }
    },

    /**
     * The South.
     */
    SOUTH(519) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetY < 0 && Math.abs(offsetX) <= -offsetY;
        }
    },

    /**
     * The South west.
     */
    SOUTH_WEST(520) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetY < 0 && offsetX < 0;
        }
    },

    /**
     * The West.
     */
    WEST(521) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetX < 0 && Math.abs(offsetY) <= -offsetX;
        }
    },

    /**
     * The North west.
     */
    NORTH_WEST(514) {
        @Override
        public boolean isInSight(int offsetX, int offsetY) {
            return offsetY > 0 && offsetX < 0;
        }
    };

    private final int animationId;

    DMCRevolution(int animationId) {
        this.animationId = animationId;
    }

    /**
     * Gets animation id.
     *
     * @return the animation id
     */
    public int getAnimationId() {
        return animationId;
    }

    /**
     * Is in sight boolean.
     *
     * @param offsetX the offset x
     * @param offsetY the offset y
     * @return the boolean
     */
    public boolean isInSight(int offsetX, int offsetY) {
        return false;
    }
}