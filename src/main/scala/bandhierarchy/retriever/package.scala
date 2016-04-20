package bandhierarchy

package object retriever {
  /**
    * API key for songkick
    */
  val apiKey = conf.getString("apiKey")

  /**
    * The API endpoint for songkick
    */
  val apiEndpoint = "http://api.songkick.com/api/3.0"
}
