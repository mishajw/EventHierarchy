package bandhierarchy.back.retriever

import bandhierarchy.back.{Band, Gig}
import org.json4s.DefaultFormats

import scala.io.Source

import org.json4s.native.JsonMethods._

object GigRetriever {

  /**
    * Case classes for JSON parsing
    */
  case class Response(resultsPage: ResultsPage)
  case class ResultsPage(status: String, results: Results, perPage: Int, page: Int, totalEntries: Int)
  case class Results(event: Seq[Event])
  case class Event(performance: Seq[Performers])
  case class Performers(billing: String, artist: Artist)
  case class Artist(displayName: String, id: Int)
    { def toBand = Band(displayName, id) }

  /**
    * Get a band's gigs
    * @param band the band to get for
    * @return the band's gigs
    */
  def run(band: Band): Seq[Gig] = {
    responseForBand(band).resultsPage.results.event
      .map(_.performance partition { p => p.billing == "headline" })
      .collect {
        case (Seq(headline), support) =>
          Gig(headline.artist.toBand, support map (_.artist.toBand))
      }
  }

  /**
    * Get the response case class for a band
    */
  private def responseForBand(band: Band): Response = {
    implicit val formats = DefaultFormats

    val url = urlForBand(band)
    val rawJson = (Source fromURL url).mkString
    val json = parse(rawJson)
    json.extract[Response]
  }

  /**
    * Get the url for a band
    */
  private def urlForBand(band: Band) = {
    s"$apiEndpoint/artists/${band.id}/gigography.json?per_page=50&order=desc&apikey=$apiKey"
  }
}
