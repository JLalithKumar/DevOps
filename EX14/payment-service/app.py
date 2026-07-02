from flask import Flask
import time

from opentelemetry import trace

from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter

from opentelemetry.instrumentation.flask import FlaskInstrumentor


trace.set_tracer_provider(TracerProvider())

span_processor = BatchSpanProcessor(
    OTLPSpanExporter(endpoint="http://tempo:4318/v1/traces")
)

trace.get_tracer_provider().add_span_processor(span_processor)


app = Flask(__name__)

FlaskInstrumentor().instrument_app(app)


@app.route("/payment")
def payment():

    # Simulate a slow payment gateway
    time.sleep(4)

    return "Payment Completed"


@app.route("/")
def home():

    return "Payment Service Running"


app.run(host="0.0.0.0", port=5002)