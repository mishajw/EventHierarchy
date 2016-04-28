package bandhierarchy.back.retriever

import java.net.URLEncoder

import bandhierarchy.back.Band
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

import scala.io.Source

object BandRetriever {

  case class Response(resultsPage: ResultsPage)
  case class ResultsPage(status: String, results: Results, perPage: Int, page: Int, totalEntries: Int)
  case class Results(artist: Seq[Artist])
  case class Artist(displayName: String, id: Int)
    { def toBand = Band(displayName, id) }

  def run(name: String): Option[Band] = {
    val response = responseForName(name)

    response.resultsPage.results.artist
      .map { _.toBand }
      .headOption
  }

  /**
    * Get the response case class for a band
    */
  private def responseForName(name: String): Response = {
    implicit val formats = DefaultFormats

    val url = urlForName(name)
    val rawJson = (Source fromURL url).mkString
    val json = parse(rawJson)
    json.extract[Response]
  }

  def urlForName(name: String) =
    s"$apiEndpoint/search/artists.json?query=${URLEncoder.encode(name, "UTF-8")}&apikey=$apiKey"
}
