package technology.zim.data

import technology.zim.World



@JvmInline
value class Tile(private val value: ULong) {

    constructor(x: Int, y: Int): this(pack(x, y)) {

    }

    fun connect(candidateTile: Tile) {
        val dir = toward(candidateTile)
        connect(dir)
    }

    //Connect two tiles together.
    //Calls utility function on the connected cell
    fun connect(dir: Directions) {
        val candidateTile = this+dir

        //Ensure that the tile is within bounds
        if(candidateTile.isInBounds() && this.isInBounds())
        {
            if(this.y() == World.sizeY - 1 && dir == Directions.DOWN)
                println("wat")
            World.update(this, getProperties().add(dir))
            World.update(candidateTile, candidateTile.getProperties().add(candidateTile.toward(this)))
        }
        else {
            //Should also never get to the point where the attempt is made
            println("Attempted to connect to outside bounds: <" +
                    candidateTile.x() + ", " + candidateTile.y()
                    + "> From Tile: <" + x() + ", " +
                    y() + ">")
            return
        }
    }

    fun toward(otherTile: Tile): Directions {
        return Directions.convertModifier(otherTile.value - this.value)
    }
    fun getAdjacentTiles(explored:Boolean): Set<Tile> {
        val adj = mutableSetOf<Tile>()
        val dirs = Directions.ALL

        dirs.forEach { dir ->
            val candidateTile = this + dir

            //Ensure that the tile is within bounds
            if(candidateTile.isInBounds() && candidateTile.getProperties().visited() == explored)
            {
                println("$this+$dir --> $candidateTile")
                adj.add(candidateTile)
            }
        }
        if(adj.isEmpty() && explored)
            println("no explored found")
        return adj
    }



    fun hasConnections(): Boolean {
        return getProperties().connections != 0
    }


    //Arguments could be made for either World or Tile knowing whether a Tile is in bounds
    fun isInBounds(): Boolean {
        return x() >= 0 &&
            y() >= 0 &&
            x() < World.tiles.value.size  &&
            y() < World.tiles.value.get(0).size
    }

    //Get the properties of the tile at the given coordinates
    fun getProperties(): TileProperties {
        return World.tiles.value.get(x()).get(y())
    }

    //Get tile at given direction
    operator fun plus(dir: Directions): Tile {
        return this + Directions.getModifier(dir)
    }

    operator fun plus(mod: ULong): Tile {
        return Tile(this.x() + x(mod), this.y() + y(mod))
    }


    fun getCoordinates(): Pair<Int, Int> {
        return Pair(x(), y())
    }

    fun x(): Int {
        return x(value)
    }

    //Gets the x value of the Long as though it were in coordinate form
    fun x(coords: ULong): Int {
        return (coords shr 32).toLong().toInt()
    }

    //Bitwise and to ensure the left-hand half of the Long is zero'd, then convert toInt()

    fun y():Int {
        return y(value)
    }

    //Get the y value from a Long as though it were a Tile
    fun y(coords: ULong): Int {
        return coords.toLong().toInt()
    }

    override fun toString():String {
        return "<" + x() + ", " + y() + ">"
    }

    companion object {
        fun pack(mostSignificantBits: Int, leastSignificantBits: Int): ULong {
            return (mostSignificantBits.toUInt().toULong() shl 32) or leastSignificantBits.toUInt().toULong()
        }
    }

}