import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min


data class AlmanachEntry(
    val source: String,
    val target: String,
    val ranges: MutableList<Range> = mutableListOf()
)

data class Range(
    val destinationRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long
)

const val FLOW_CHUNK_SIZE = 10_000
const val FLOW_COLLECT_CHUNK_SIZE = 500
const val THREADS = 24
var sourcedAlmanach: Map<String, AlmanachEntry> = emptyMap()

@OptIn(DelicateCoroutinesApi::class)
val customDispatcher = newFixedThreadPoolContext(THREADS, "customDispatcher")

class Day05 {
    companion object {
        fun part01(lines: List<String>): Long {
            val seeds = lines.first().split("\\s".toRegex()).drop(1).map { it.toLong() }
            val almanach = createAlmanach(lines)
            sourcedAlmanach = almanach.associateBy { it.source }
            return findDestination(seeds, "seed", "location")
        }

        fun part02(lines: List<String>): Long {
            val seeds = lines.first().split("\\s".toRegex()).drop(1).map { it.toLong() }
            val seedPairs = seeds.chunked(2)
            val seedRanges = seedPairs.map { (it[0] until (it[0] + it[1])) }
            val almanach = createAlmanach(lines)
            sourcedAlmanach = almanach.associateBy { it.source }
            return parallelProcess(seedRanges.asSequence().flatten().chunked(FLOW_CHUNK_SIZE).asFlow())
        }


    }
}

fun parallelProcess(flow: Flow<List<Long>>): Long {
    val minValue = AtomicLong(Long.MAX_VALUE)
    runBlocking {
        withContext(customDispatcher) {
            flow.map { seeds ->
                async {
                    seeds.map { seed ->
                        findNextDestinationRecursive(
                            seed,
                            "seed",
                            "location"
                        )
                    }
                }
            }.buffer(FLOW_COLLECT_CHUNK_SIZE).map { it.await() }
                .map {
                val listMin = it.min()
                synchronized(minValue) {
                    minValue.set(
                        min(
                            listMin, minValue.get()
                        )
                    )
                }
            }
                .collect()
        }
    }
    return minValue.get()
}

fun createAlmanach(lines: List<String>): List<AlmanachEntry> {
    val almanach: MutableList<AlmanachEntry> = mutableListOf()
    var currentEntry: AlmanachEntry? = null
    for (line in lines.drop(1)) {
        if (line.isEmpty()) {
            if (currentEntry != null) {
                almanach.add(currentEntry)
            }
            currentEntry = null
        } else if (line.contains("map")) {
            val split = line.split("-")
            val source = split.first()
            val target = split[2].split("\\s".toRegex()).first()
            currentEntry = AlmanachEntry(source = source, target = target)
        } else if (currentEntry != null) {
            val rangeList = line.split("\\s".toRegex()).map { it.toLong() }
            val range = Range(rangeList[0], rangeList[1], rangeList[2])
            currentEntry.ranges.add(range)
        }
    }
    if (currentEntry != null) {
        almanach.add(currentEntry)
    }
    return almanach.toList()
}

fun findDestination(
    seeds: List<Long>,
    start: String,
    destination: String
): Long =
    seeds.minOf { seed ->
        findNextDestinationRecursive(seed, start, destination)
    }

fun findNextDestinationRecursive(
    currentNumber: Long,
    currentSource: String,
    destination: String
): Long {
    val entry = sourcedAlmanach[currentSource]
    if (entry == null || entry.source == destination) {
        return currentNumber
    }

    val next = entry.target
    val validRangeEntry = entry.ranges.find { rangeEntry ->
        rangeEntry.sourceRangeStart <= currentNumber && currentNumber <= rangeEntry.sourceRangeStart + rangeEntry.rangeLength

    }
    return if (validRangeEntry != null) {
        findNextDestinationRecursive(
            currentNumber - validRangeEntry.sourceRangeStart + validRangeEntry.destinationRangeStart,
            next,
            destination
        )
    } else {
        return findNextDestinationRecursive(
            currentNumber, next, destination
        )
    }
}