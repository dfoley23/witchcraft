package com.anythingmachine.witchcraft.agents.player.items;

public enum InventoryType {
    PIGEYEBALL {
	@Override
	public float getMana() {
	    return 4;
	}

	@Override
	public float getPower() {
	    return 2;
	}

	@Override
	public float getPH() {
	    return 7;
	}

	@Override
	public float getConsistency() {
	    return 3;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.SAGE;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.CROWFEATHER;
	}

    },
    CROWFEATHER {
	@Override
	public float getMana() {
	    return 7;
	}

	@Override
	public float getPower() {
	    return 3;
	}

	@Override
	public float getPH() {
	    return 3;
	}

	@Override
	public float getConsistency() {
	    return 1;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.HUMANBONE;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.GHOSTBREATH;
	}

    },
    FROGLEG {
	@Override
	public float getMana() {
	    return 3;
	}

	@Override
	public float getPower() {
	    return 4;
	}

	@Override
	public float getPH() {
	    return 3;
	}

	@Override
	public float getConsistency() {
	    return 3;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.PIGEYEBALL;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SAGE;
	}

    },
    SAGE {
	@Override
	public float getMana() {
	    return 5;
	}

	@Override
	public float getPower() {
	    return 5;
	}

	@Override
	public float getPH() {
	    return 5;
	}

	@Override
	public float getConsistency() {
	    return 2;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.CATSKULL;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SCRAPSILVER;
	}

    },
    CATSKULL {
	@Override
	public float getMana() {
	    return 7;
	}

	@Override
	public float getPower() {
	    return 4;
	}

	@Override
	public float getPH() {
	    return 1;
	}

	@Override
	public float getConsistency() {
	    return 0;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.SCRAPSILVER;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SCRAPGOLD;
	}

    },
    HUMANBONE {
	@Override
	public float getMana() {
	    return 6;
	}

	@Override
	public float getPower() {
	    return 5;
	}

	@Override
	public float getPH() {
	    return 3;
	}

	@Override
	public float getConsistency() {
	    return 0;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.SAGE;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SCRAPSTEEL;
	}

    },
    SCRAPSTEEL {
	@Override
	public float getMana() {
	    return 0;
	}

	@Override
	public float getPower() {
	    return 8;
	}

	@Override
	public float getPH() {
	    return 1;
	}

	@Override
	public float getConsistency() {
	    return 0;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.CATSKULL;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SCRAPGOLD;
	}

    },
    OAKBARK {
	@Override
	public float getMana() {
	    return 4;
	}

	@Override
	public float getPower() {
	    return 6;
	}

	@Override
	public float getPH() {
	    return 5;
	}

	@Override
	public float getConsistency() {
	    return 4;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.CROWFEATHER;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.HUMANBONE;
	}

    },
    GHOSTBREATH {
	@Override
	public float getMana() {
	    return 9;
	}

	@Override
	public float getPower() {
	    return 5;
	}

	@Override
	public float getPH() {
	    return 8;
	}

	@Override
	public float getConsistency() {
	    return 8;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.SAGE;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.CATSKULL;
	}
    },
    SCRAPSILVER {
	@Override
	public float getMana() {
	    return 7;
	}

	@Override
	public float getPower() {
	    return 7;
	}

	@Override
	public float getPH() {
	    return 3;
	}

	@Override
	public float getConsistency() {
	    return 3;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.OAKBARK;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SCRAPGOLD;
	}

    },
    SCRAPGOLD {
	@Override
	public float getMana() {
	    return 4;
	}

	@Override
	public float getPower() {
	    return 7;
	}

	@Override
	public float getPH() {
	    return 5;
	}

	@Override
	public float getConsistency() {
	    return 2;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.HUMANBONE;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.SCRAPSILVER;
	}
    },
    SCRAPGRANITE {
	@Override
	public float getMana() {
	    return 2;
	}

	@Override
	public float getPower() {
	    return 7;
	}

	@Override
	public float getPH() {
	    return 3;
	}

	@Override
	public float getConsistency() {
	    return 1;
	}

	@Override
	public InventoryType getCompliment() {
	    return InventoryType.OAKBARK;
	}

	@Override
	public InventoryType getUnCompliment() {
	    return InventoryType.PIGEYEBALL;
	}
    };
    public float getMana() {
	return 2;
    }

    public float getPower() {
	return 0;
    }

    public float getPH() {
	return 5;
    }

    public float getConsistency() {
	return 5;
    }

    public InventoryType getCompliment() {
	return InventoryType.SAGE;
    }

    public InventoryType getUnCompliment() {
	return InventoryType.HUMANBONE;
    }
}
