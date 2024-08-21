import org.apache.spark.sql.SparkSession

object HelloWorld {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("HelloWorldApp").getOrCreate()
    val data = Seq("Hello World")
    val df = spark.createDataFrame(data.map(Tuple1(_))).toDF("message")
    df.show()
    log.info("Hello Spark !")
  }
}