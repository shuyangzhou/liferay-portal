/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
// DWB20: ThreadIO should check and reset IO if something (e.g. jetty) overrides
package org.apache.felix.gogo.runtime.threadio;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Deque;
import java.util.LinkedList;

import org.apache.felix.service.threadio.ThreadIO;

public class ThreadIOImpl implements ThreadIO
{
	final Marker defaultMarker = new Marker(System.in, System.out, System.err, null);
    final ThreadPrintStream err = new ThreadPrintStream(this, System.err, true);
    final ThreadPrintStream out = new ThreadPrintStream(this, System.out, false);
    final ThreadInputStream in = new ThreadInputStream(this, System.in);
    final ThreadLocal<Deque<Marker>> current = new InheritableThreadLocal<Deque<Marker>>()
    {
        @Override
        protected Deque<Marker> initialValue()
        {
            return new LinkedList<Marker>();
        }
    };

    public void start()
    {
    }

    public void stop()
    {
    }

    Marker current()
    {
        Deque<Marker> markers = current.get();

        return markers.peek();
    }

    public void close()
    {
        Deque<Marker> markers = current.get();

		markers.pop();

		if (markers.isEmpty()) {
			System.setOut(defaultMarker.out);
			System.setIn(defaultMarker.in);
			System.setErr(defaultMarker.err);

			current.remove();
		}
    }

    public void setStreams(InputStream in, PrintStream out, PrintStream err)
    {
        assert in != null;
        assert out != null;
        assert err != null;

		Deque<Marker> markers = current.get();

		if (markers.isEmpty()) {
			System.setOut(out);
			System.setIn(in);
			System.setErr(err);
		}

		markers.push(new Marker(in, out, err, null));
    }
}
/* @generated */