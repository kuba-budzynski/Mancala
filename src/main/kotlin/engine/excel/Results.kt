package engine.excel

import engine.game.Move
import engine.game.Stats
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class Results() {

    private val workbook = XSSFWorkbook()
    private val createHelper = workbook.creationHelper
    private val headerFont = workbook.createFont()
    private val headerCellStyle = workbook.createCellStyle()
    private val games = mutableMapOf<Int, MutableList<Move>>()
    public val stats = mutableListOf<Stats>()

    private val columns = listOf("N", "Winner", "Total time", "Total moves", "Steals", "Moves", "Time to first steal",
        "Moves to first steal", "Time to first bonus", "Moves to first bonus", "Score")

    private val gameColumns = listOf("N", "Player", "Bucket", "Stones", "Time", "Was a bonus move", "Has stones been stolen")

    fun generateResults(config: List<Triple<Int, String, List<String>>>, fileName: String = "res.xlsx"){
        var sheet = workbook.createSheet("Results")

        headerFont.boldweight
        headerFont.color = IndexedColors.RED.index
        headerCellStyle.setFont(headerFont)

        var headerRow = sheet.createRow(0)
        for(col in columns.indices){
            val cell = headerRow.createCell(col)
            cell.setCellValue(columns[col])
            cell.cellStyle = headerCellStyle
        }

        var rowIdx = 1
        for(stat in stats){
            val row = sheet.createRow(rowIdx)
            row.createCell(0).setCellValue(rowIdx.toDouble())
            row.createCell(1).setCellValue(stat.player)
            row.createCell(2).setCellValue(stat.totalTime.toDouble())
            row.createCell(3).setCellValue(stat.totalMoves.toDouble())
            row.createCell(4).setCellValue(stat.steals.toDouble())
            row.createCell(5).setCellValue(stat.bonusMoves.toDouble())
            row.createCell(6).setCellValue(stat.timeToFirstSteal.toDouble())
            row.createCell(7).setCellValue(stat.movesToFirstSteal.toDouble())
            row.createCell(8).setCellValue(stat.timeToFirstBonus.toDouble())
            row.createCell(9).setCellValue(stat.movesToFirstBonus.toDouble())
            row.createCell(10).setCellValue(stat.score)
            rowIdx++
        }
        for(column in columns.indices){
            sheet.autoSizeColumn(column)
        }

        var starting = 12
        val table = listOf("Depth", "Method", "Heuristic")
        for(t in config.indices){
            starting += t
            for(tab in table.indices){
                val cell = sheet.getRow(0).createCell(starting++)
                cell.setCellValue(table[tab] + "-${t+1}")
                cell.cellStyle = headerCellStyle
            }
        }

        sheet.getRow(1).createCell(12).setCellValue(config[0].first.toDouble())
        sheet.getRow(1).createCell(13).setCellValue(config[0].second)
        sheet.getRow(1).createCell(16).setCellValue(config[1].first.toDouble())
        sheet.getRow(1).createCell(17).setCellValue(config[1].second)

        val heuristics = listOf(14,18)
        for(t in heuristics.indices){
            var start = 1
            for(h in config[t].third){
                sheet.getRow(start++).createCell(heuristics[t]).setCellValue(h)
            }
        }

        starting = 12
        for(t in config.indices){
            starting += t
            for(tab in table.indices){
                sheet.autoSizeColumn(starting++)
            }
        }

        for(game in games.keys){
            sheet = workbook.createSheet("Game-${game+1}");
            headerRow = sheet.createRow(0)
            // Columns
            for(col in gameColumns.indices){
                val cell = headerRow.createCell(col)
                cell.setCellValue(gameColumns[col])
                cell.cellStyle = headerCellStyle
            }
            // Rows
            rowIdx = 1
            for(move in games[game]!!){
                val row = sheet.createRow(rowIdx)
                row.createCell(0).setCellValue(rowIdx.toDouble())
                row.createCell(1).setCellValue(move.player.name)
                row.createCell(2).setCellValue(move.bucket.toDouble())
                row.createCell(3).setCellValue(move.stones.toDouble())
                row.createCell(4).setCellValue(move.time.toDouble())
                row.createCell(5).setCellValue(move.wasBonus)
                row.createCell(6).setCellValue(move.steals)
                rowIdx++
            }
            for(column in gameColumns.indices){
                sheet.autoSizeColumn(column)
            }
        }

        val fileOut = FileOutputStream(fileName)
        workbook.write(fileOut)
        fileOut.close()
    }

    fun addMove(i: Int, move: Move){
        val g = games.getOrDefault(i, mutableListOf())
        g.add(move)
        games[i] = g
    }

    fun addStat(stat: Stats){
        stats.add(stat)
    }
}